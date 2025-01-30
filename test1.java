@Mapper(componentModel = "spring")
public interface PaymentVerificationMapper {

    PaymentVerificationMapper INSTANCE = Mappers.getMapper(PaymentVerificationMapper.class);

    // Map Order to OrderInfoDto
    @Mapping(source = "sbiOrderRefNumber", target = "sbiOrderId", defaultValue = "")
    @Mapping(source = "orderRefNumber", target = "merchantOrderNumber", defaultValue = "")
    @Mapping(source = "status", target = "orderStatus", defaultValue = "")
    @Mapping(source = "currencyCode", target = "currency", defaultValue = "")
    OrderInfoDto orderToOrderInfoDto(Order order);

    // Map Transaction to PaymentVerificationDto
    @Mapping(source = "atrnNumber", target = "atrn", defaultValue = "")
    @Mapping(source = "orderAmount", target = "orderAmount", defaultValue = "0")
    @Mapping(source = "debitAmount", target = "totalAmount", defaultValue = "0")
    @Mapping(source = "transactionStatus", target = "transactionStatus", defaultValue = "")
    @Mapping(source = "payMode", target = "payMode", defaultValue = "")
    @Mapping(source = "channelBank", target = "bankName", defaultValue = "")
    @Mapping(source = "bankReferenceNumber", target = "bankTxnNumber", defaultValue = "")
    @Mapping(source = "payProcId", target = "processor", defaultValue = "")
    @Mapping(source = "createdDate", target = "transactionTime", defaultValue = "0")
    @Mapping(source = "cin", target = "CIN", defaultValue = "")
    PaymentVerificationDto transactionToPaymentVerificationDto(Transaction transaction);
}

@Service
public class PaymentVerificationService {

    private final PaymentVerificationMapper paymentVerificationMapper;

    public PaymentVerificationService(PaymentVerificationMapper paymentVerificationMapper) {
        this.paymentVerificationMapper = paymentVerificationMapper;
    }

    public PaymentVerificationResponse buildPaymentVerificationResponse(List<Object[]> transactionOrderList) {
        logger.info("Mapping Order data.");

        // Retrieve the first order data and map to OrderInfoDto using MapStruct
        Object[] firstRecord = transactionOrderList.get(0);
        Order firstOrder = (Order) firstRecord[1];
        OrderInfoDto orderDto = paymentVerificationMapper.orderToOrderInfoDto(firstOrder);

        logger.info("Mapping Transaction data.");
        // Map transactions to PaymentVerificationDto using MapStruct
        List<PaymentVerificationDto> transactionsDTOs = transactionOrderList.stream()
                .map(record -> paymentVerificationMapper.transactionToPaymentVerificationDto((Transaction) record[0]))
                .collect(Collectors.toList());

        logger.info("Transaction and Order data mapped.");
        // Build and return the response
        return PaymentVerificationResponse.builder()
                .paymentInfo(transactionsDTOs)
                .orderInfo(orderDto)
                .build();
    }
}

