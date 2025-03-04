ALTER TABLE MERCHANT_ORDER_PAYMENTS ADD  PUSH_STATUS VARCHAR2(10);


ALTER TABLE MERCHANT_ORDER_PAYMENTS 
ADD POOLING_STATUS VARCHAR2(10) DEFAULT 'P' NOT NULL;

UPDATE MERCHANT_ORDER_PAYMENTS 
SET POOLING_STATUS = 'P' 
WHERE POOLING_STATUS IS NULL;
COMMIT;


@Query("SELECT mopDtl FROM MerchantOrderPaymentEntity mopDtl WHERE mopDtl.transactionStatus IN ('BOOKED', 'PENDING') " +
            "AND mopDtl.paymentStatus IN ('PAYMENT_INITIATION_START', 'PENDING')")
    List<MerchantOrderPaymentEntity> findMerchantOrderPaymentDetails();

@Query("SELECT mopDtl FROM MerchantOrderPaymentEntity mopDtl WHERE " +
       "(mopDtl.transactionStatus IN ('BOOKED', 'PENDING') AND mopDtl.paymentStatus IN ('PAYMENT_INITIATION_START', 'PENDING')) " +
       "AND mopDtl.poolingStatus = 'P'")

            

@Modifying
@Query("UPDATE MerchantOrderPaymentEntity mopDtl SET mopDtl.poolingStatus = 'Q' WHERE mopDtl.id IN :ids")
int updatePoolingStatusToQ(@Param("ids") List<Long> ids);
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

...

// Grouping by payMode
Map<String, List<MerchantOrderPaymentEntity>> groupedByPayMode = merchantOrderPaymentList.stream()
        .collect(Collectors.groupingBy(MerchantOrderPaymentEntity::getPayMode));

System.out.println("INB Transactions: " + groupedByPayMode.get("INB"));
System.out.println("Other INB Transactions: " + groupedByPayMode.get("otherINB"));


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

...

// First, group by payMode (INB / otherINB)
Map<String, Map<String, List<MerchantOrderPaymentEntity>>> groupedByPayModeAndStatus = merchantOrderPaymentList.stream()
        .collect(Collectors.groupingBy(
                MerchantOrderPaymentEntity::getPayMode,
                Collectors.groupingBy(MerchantOrderPaymentEntity::getTransactionStatus)
        ));

// Extract groups
List<MerchantOrderPaymentEntity> inbBooked = groupedByPayModeAndStatus.getOrDefault("INB", Map.of()).getOrDefault("BOOKED", List.of());
List<MerchantOrderPaymentEntity> inbPending = groupedByPayModeAndStatus.getOrDefault("INB", Map.of()).getOrDefault("PENDING", List.of());

List<MerchantOrderPaymentEntity> otherInbBooked = groupedByPayModeAndStatus.getOrDefault("otherINB", Map.of()).getOrDefault("BOOKED", List.of());
List<MerchantOrderPaymentEntity> otherInbPending = groupedByPayModeAndStatus.getOrDefault("otherINB", Map.of()).getOrDefault("PENDING", List.of());

System.out.println("INB - BOOKED: " + inbBooked);
System.out.println("INB - PENDING: " + inbPending);
System.out.println("otherINB - BOOKED: " + otherInbBooked);
System.out.println("otherINB - PENDING: " + otherInbPending);


