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

    public boolean processPaymentPushVerification(PaymentPushVerificationDto paymentPushVerificationDto) throws JsonProcessingException {
        logger.info("Starting payment push verification process for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());

        Optional<List<Object[]>> transactionOptional = transactionDao.getTransactionAndOrderDetail(paymentPushVerificationDto);

        if (transactionOptional.isEmpty()) {
            logger.error("Transaction data not found for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
            return false;
        }

        logger.info("Transaction data found. Mapping order and payment info.");
        PaymentVerificationResponse paymentVerificationResponse = buildPaymentVerificationResponse(transactionOptional.get());

        if (!paymentVerificationResponse.getOrderInfo().getReturnUrl().equalsIgnoreCase(PushResponseStatus)) {
            logger.warn("Return URL mismatch for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
            return false;
        }

        logger.info("Fetching encryption key for Merchant ID: {}", paymentPushVerificationDto.getMId());
        EncryptionKeyDto encryptionKeyDto = kmsServiceClient.getEncryptionKey(paymentPushVerificationDto.getMId());

        String encryptedPaymentPushData = encryptionDecryptionUtil.encryptRequest(
                objectMapper.writeValueAsString(paymentVerificationResponse), encryptionKeyDto);
        logger.debug("Encrypted payment push data: {}", encryptedPaymentPushData);

        logger.info("Sending encrypted payment push data to merchant service.");
        boolean merchantPushResponseFlag = merchantServiceClient.postPaymentPushVerification(encryptedPaymentPushData);

        if (!merchantPushResponseFlag) {
            logger.error("Merchant push verification failed for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
            return false;
        }

        logger.info("Merchant push verification successful. Updating push verification status.");
        boolean updateStatus = transactionDao.updatePushVerificationStatus(paymentPushVerificationDto.getAtrnNumber(), "Y") > 0;

        if (updateStatus) {
            logger.info("Push verification status updated successfully for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
        } else {
            logger.warn("Failed to update push verification status for ATRN: {}", paymentPushVerificationDto.getAtrnNumber());
        }

        return updateStatus;
    }
}
