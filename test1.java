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


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MerchantOrderPaymentService {

    public static void main(String[] args) {
        // Fetch data
        List<MerchantOrderPaymentEntity> merchantOrderPaymentList = fetchMerchantData();

        // Group by payMode (INB / otherINB)
        Map<String, List<MerchantOrderPaymentEntity>> groupedByPayMode = merchantOrderPaymentList.stream()
                .collect(Collectors.groupingBy(MerchantOrderPaymentEntity::getPayMode));

        // Separate INB and otherINB
        List<MerchantOrderPaymentEntity> inbList = groupedByPayMode.getOrDefault("INB", List.of());
        List<MerchantOrderPaymentEntity> otherInbList = groupedByPayMode.getOrDefault("otherINB", List.of());

        // Handle INB transactions separately
        handleINBTransactions(inbList);

        // Handle otherINB transactions separately
        handleOtherINBTransactions(otherInbList);
    }

    private static void handleINBTransactions(List<MerchantOrderPaymentEntity> inbList) {
        // Separate BOOKED and PENDING within INB
        List<MerchantOrderPaymentEntity> bookedINB = inbList.stream()
                .filter(mop -> "BOOKED".equals(mop.getTransactionStatus()))
                .collect(Collectors.toList());

        List<MerchantOrderPaymentEntity> pendingINB = inbList.stream()
                .filter(mop -> "PENDING".equals(mop.getTransactionStatus()))
                .collect(Collectors.toList());

        // Custom handling logic
        processBookedTransactions(bookedINB);
        processPendingTransactions(pendingINB);
    }

    private static void handleOtherINBTransactions(List<MerchantOrderPaymentEntity> otherInbList) {
        // Separate BOOKED and PENDING within otherINB
        List<MerchantOrderPaymentEntity> bookedOtherINB = otherInbList.stream()
                .filter(mop -> "BOOKED".equals(mop.getTransactionStatus()))
                .collect(Collectors.toList());

        List<MerchantOrderPaymentEntity> pendingOtherINB = otherInbList.stream()
                .filter(mop -> "PENDING".equals(mop.getTransactionStatus()))
                .collect(Collectors.toList());

        // Custom handling logic
        processBookedTransactions(bookedOtherINB);
        processPendingTransactions(pendingOtherINB);
    }

    private static void processBookedTransactions(List<MerchantOrderPaymentEntity> bookedList) {
        // Your custom handling logic for BOOKED transactions
        bookedList.forEach(mop -> System.out.println("Processing BOOKED transaction: " + mop));
    }

    private static void processPendingTransactions(List<MerchantOrderPaymentEntity> pendingList) {
        // Your custom handling logic for PENDING transactions
        pendingList.forEach(mop -> System.out.println("Processing PENDING transaction: " + mop));
    }

    private static List<MerchantOrderPaymentEntity> fetchMerchantData() {
        // Mock method to fetch data
        return List.of(
            new MerchantOrderPaymentEntity(1L, "BOOKED", "PENDING", "P", "ATRN123", "INB"),
            new MerchantOrderPaymentEntity(2L, "PENDING", "PAYMENT_INITIATION_START", "P", "ATRN456", "otherINB"),
            new MerchantOrderPaymentEntity(3L, "BOOKED", "PENDING", "P", "ATRN789", "INB"),
            new MerchantOrderPaymentEntity(4L, "PENDING", "PAYMENT_INITIATION_START", "P", "ATRN987", "otherINB"),
            new MerchantOrderPaymentEntity(5L, "BOOKED", "PENDING", "P", "ATRN741", "otherINB"),
            new MerchantOrderPaymentEntity(6L, "PENDING", "PAYMENT_INITIATION_START", "P", "ATRN852", "INB")
        );
    }
}



