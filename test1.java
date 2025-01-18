@Query("SELECT o.sbiOrderRefNumber FROM Orders o " +
       "JOIN Transaction t ON o.sbiOrderRefNumber = t.sbiOrderRefNumber " +
       "WHERE o.status = 'ATTEMPTED' " +
       "AND t.transactionStatus = 'BOOKED' " +
       "AND t.paymentStatus = 'PAYMENT_INITIATED_START'")
List<String> findSbiOrderRefNumbersForUpdate();

@Modifying
@Query("UPDATE Transaction t " +
       "SET t.transactionStatus = 'FAILED' " +
       "WHERE t.sbiOrderRefNumber IN :sbiOrderRefNumbers")
void updateTransactionStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);

@Modifying
@Query("UPDATE Orders o " +
       "SET o.status = 'FAILED' " +
       "WHERE o.sbiOrderRefNumber IN :sbiOrderRefNumbers")
void updateOrderStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);


To update both the Transaction and Order tables in one go, considering that some conditions might change after updating the Transaction table, you can do the following:

1. Temporary Table or In-Memory Collection: Use an in-memory collection to keep track of sbiOrderRefNumbers that meet the initial conditions. Then, use this collection to update both the Transaction and Order tables.


2. Single Transaction Block: Execute the updates within a single transaction to ensure atomicity. First, select the sbiOrderRefNumbers that meet the conditions, then perform the updates on both tables.



Approach:

Repository Methods:

You will need to fetch the relevant sbiOrderRefNumbers first and then use these to update the Transaction and Order tables.

Fetch sbiOrderRefNumbers Meeting Initial Conditions:

@Query("SELECT o.sbiOrderRefNumber FROM Orders o " +
       "JOIN Transaction t ON o.sbiOrderRefNumber = t.sbiOrderRefNumber " +
       "WHERE o.status = 'ATTEMPTED' " +
       "AND t.transactionStatus = 'BOOKED' " +
       "AND t.paymentStatus = 'PAYMENT_INITIATED_START'")
List<String> findSbiOrderRefNumbersForUpdate();

Update Transaction Table:

@Modifying
@Query("UPDATE Transaction t " +
       "SET t.transactionStatus = 'FAILED' " +
       "WHERE t.sbiOrderRefNumber IN :sbiOrderRefNumbers")
void updateTransactionStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);

Update Order Table:

@Modifying
@Query("UPDATE Orders o " +
       "SET o.status = 'FAILED' " +
       "WHERE o.sbiOrderRefNumber IN :sbiOrderRefNumbers")
void updateOrderStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);

Service Layer Method:

@Service
public class OrderTransactionService {

    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;

    public OrderTransactionService(OrderRepository orderRepository, TransactionRepository transactionRepository) {
        this.orderRepository = orderRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void updateStatusForOrdersAndTransactions() {
        // Fetch the sbiOrderRefNumbers that meet the initial conditions
        List<String> sbiOrderRefNumbers = orderRepository.findSbiOrderRefNumbersForUpdate();

        // Update transaction statuses
        transactionRepository.updateTransactionStatusToFailed(sbiOrderRefNumbers);
        
        // Update order statuses
        orderRepository.updateOrderStatusToFailed(sbiOrderRefNumbers);
    }
}

Explanation:

1. Fetch Matching Records: The method findSbiOrderRefNumbersForUpdate fetches the sbiOrderRefNumbers that meet the specified conditions in both the Order and Transaction tables.


2. Update Transaction Table: Use the fetched sbiOrderRefNumbers to update the Transaction table.


3. Update Order Table: After updating the Transaction table, update the Order table using the same sbiOrderRefNumbers.


4. Transactional Integrity: Since both updates occur within a single transactional method, any failure in one part of the operation will roll back the entire transaction, ensuring data consistency.



By fetching the sbiOrderRefNumbers that meet the conditions initially and then using this list to update both tables, you can handle the updates in a single transactional context.

