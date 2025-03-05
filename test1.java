Caused by: java.lang.IllegalArgumentException: org.hibernate.query.SemanticException: Missing constructor for type 'PaymentVerificationDto' [SELECT new com.epay.transaction.dto.PaymentVerificationDto(t.atrnNumber AS atrn, t.orderAmount, t.orderAmount AS totalAmount, t.transactionStatus, t.payMode, t.channelBank AS bankName, t.bankReferenceNumber AS bankTxnNumber, t.payProcId AS processor, t.createdDate AS transactionTime, t.cin AS CIN, t.pushStatus), new com.epay.transaction.dto.OrderInfoDto(o.sbiOrderRefNumber AS sbiOrderId, o.orderRefNumber AS merchantOrderNumber, o.status AS orderStatus, o.currencyCode AS currency) FROM MerchantOrderPayment t JOIN MerchantOrder o ON t.orderRefNumber = o.orderRefNumber WHERE t.atrnNumber = :atrnNumber AND t.pushStatus = :pushStatus]

 @Query("SELECT new com.epay.transaction.dto.PaymentVerificationDto(t.atrnNumber AS atrn, t.orderAmount, t.orderAmount AS totalAmount, t.transactionStatus, t.payMode, t.channelBank AS bankName, t.bankReferenceNumber AS bankTxnNumber, t.payProcId AS processor, t.createdDate AS transactionTime, t.cin AS CIN, t.pushStatus), " +
            "new com.epay.transaction.dto.OrderInfoDto(o.sbiOrderRefNumber AS sbiOrderId, o.orderRefNumber AS merchantOrderNumber, o.status AS orderStatus, o.currencyCode AS currency) " +
            "FROM MerchantOrderPayment t " +
            "JOIN MerchantOrder o ON t.orderRefNumber = o.orderRefNumber " +
            "WHERE t.atrnNumber = :atrnNumber " +
            "AND t.pushStatus = :pushStatus")
    Optional<List<Object[]>> findTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber,
                                                           @Param("pushStatus") String pushStatus);


@Data
@AllArgsConstructor
public class PaymentVerificationDto {
    private String atrn;
    private BigDecimal orderAmount;
    private BigDecimal totalAmount;
    private String transactionStatus;
    private String payMode;
    private String bankName;
    private String bankTxnNumber;
    private String processor;
    private Long transactionTime;
    private String CIN;
    private String pushStatus;
}

@Data
@RequiredArgsConstructor
public class OrderInfoDto {
    private String sbiOrderId;
    private String merchantOrderNumber;
    private OrderStatus orderStatus;
    private String currency;
}
