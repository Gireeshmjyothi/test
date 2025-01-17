import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderTransactionRepository extends CrudRepository<OrderTransaction, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE orders o, transaction t " +
                   "SET o.status = 'FAILED', t.status = 'FAILED' " +
                   "WHERE o.sbiOrderRefNumber = t.sbiOrderRefNumber " +
                   "AND o.status = 'ATTEMPTED' " +
                   "AND t.status = 'BOOKED' " +
                   "AND t.paymentStatus = 'PAYMENT_INITIATION_START' " +
                   "AND o.sbiOrderRefNumber IN :orderRefNumbers", nativeQuery = true)
    int updateOrderAndTransactionStatusToFailed(List<String> orderRefNumbers);
}
