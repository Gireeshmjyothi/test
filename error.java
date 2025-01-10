package com.example.repository;

import com.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t " +
           "WHERE t.mid = :mid " +
           "AND t.date BETWEEN :startDate AND :endDate " +
           "ORDER BY t.date")
    List<Transaction> findTransactionsByMidAndDateRange(@Param("mid") String mid,
                                                        @Param("startDate") Date startDate,
                                                        @Param("endDate") Date endDate);
}

@Data
@Entity
@Table(name = "VIEW_RECENT_TXN")
public class ViewRecentTxn {
    @Id
    @Column(name = "MERCHANT_ID")
    private String mID;
    private int count;
    private int amount;
    private int tax;
    @Column(name = "CREATIONDATE")
    private Date creationDate;
}
package com.example.repository;

import com.example.entity.ViewRecentTxn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface ViewRecentTxnRepository extends JpaRepository<ViewRecentTxn, String> {

    @Query(value = "SELECT * FROM VIEW_RECENT_TXN v " +
                   "WHERE v.MERCHANT_ID = :mid " +
                   "AND v.CREATIONDATE BETWEEN :startDate AND :endDate " +
                   "ORDER BY v.CREATIONDATE", nativeQuery = true)
    List<ViewRecentTxn> findTransactionsByMidAndDateRange(@Param("mid") String mid,
                                                          @Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate);
}

package com.example.repository;

import com.example.entity.ViewRecentTxn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface ViewRecentTxnRepository extends JpaRepository<ViewRecentTxn, String> {

    @Query("SELECT v FROM ViewRecentTxn v " +
           "WHERE v.mID = :mid " +
           "AND v.creationDate BETWEEN :startDate AND :endDate " +
           "ORDER BY v.creationDate")
    List<ViewRecentTxn> findTransactionsByMidAndDateRange(@Param("mid") String mid,
                                                          @Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate);
}
