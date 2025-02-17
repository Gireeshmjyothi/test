import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final TransactionDao transactionDao;
    private final KmsServiceClient kmsServiceClient;
    private final EncryptionDecryptionUtil encryptionDecryptionUtil;
    private final MerchantServiceClient merchantServiceClient;
    private final ObjectMapper objectMapper;

    public PaymentService(TransactionDao transactionDao, 
                          KmsServiceClient kmsServiceClient,
                          EncryptionDecryptionUtil encryptionDecryptionUtil, 
                          MerchantServiceClient merchantServiceClient,
                          ObjectMapper objectMapper) {
        this.transactionDao = transactionDao;
        this.kmsServiceClient = kmsServiceClient;
        this.encryptionDecryptionUtil = encryptionDecryptionUtil;
        this.merchantServiceClient = merchantServiceClient;
        this.objectMapper = objectMapper;
    }

    public boolean processPaymentPushVerification(PaymentPushVerificationDto dto) throws JsonProcessingException {
        logger.info("Starting payment push verification for ATRN: {}", dto.getAtrnNumber());

        return transactionDao.getTransactionAndOrderDetail(dto)
                .map(this::buildPaymentVerificationResponse)
                .filter(response -> response.getOrderInfo().getReturnUrl().equalsIgnoreCase(PushResponseStatus))
                .map(response -> encryptAndSendToMerchant(dto, response))
                .filter(Boolean::booleanValue)
                .map(success -> updatePushVerificationStatus(dto.getAtrnNumber()))
                .orElseGet(() -> {
                    logger.warn("Push verification process failed for ATRN: {}", dto.getAtrnNumber());
                    return false;
                });
    }

    private boolean encryptAndSendToMerchant(PaymentPushVerificationDto dto, PaymentVerificationResponse response) {
        try {
            logger.info("Fetching encryption key for Merchant ID: {}", dto.getMId());
            EncryptionKeyDto encryptionKey = kmsServiceClient.getEncryptionKey(dto.getMId());
            String encryptedData = encryptionDecryptionUtil.encryptRequest(objectMapper.writeValueAsString(response), encryptionKey);
            logger.debug("Encrypted payment push data: {}", encryptedData);
            return merchantServiceClient.postPaymentPushVerification(encryptedData);
        } catch (Exception e) {
            logger.error("Error encrypting or sending payment push verification: {}", e.getMessage());
            return false;
        }
    }

    private boolean updatePushVerificationStatus(String atrnNumber) {
        boolean isUpdated = transactionDao.updatePushVerificationStatus(atrnNumber, "Y") > 0;
        logger.info("Push verification status update {} for ATRN: {}", isUpdated ? "successful" : "failed", atrnNumber);
        return isUpdated;
    }
}
