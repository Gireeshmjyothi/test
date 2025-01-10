
@Data
@Entity
@Table(name = "BANK_MASTER_VIEW")
public class BankMasterView {

    @Id
    private String bankId;
    @Column(name = "BANKNAME")
    private String bankname;
    private Long creationDate;
    private String createdBy;
    private Long modifiedDate;
    private String modifiedBy;
    private String status;
    private String bankAbbreviation;
    private String bankName;

}  

@Data
@Entity
@Table(name = "BANK_BRANCHES_VIEW")
public class BankBranchView {
    @Id
    private String branchId;
    private String bankId;
    private String branchName;
    private String ifscCode;
    private String branchAddress;
    private String city;
    private String state;
    private char status;
    private String remarks;
    private String remarks1;
    private String checkerRemark;
    private String checkerRemark1;

    private Date creationDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;

}

@Query(value = """
                SELECT t, o
                FROM Transaction t
                INNER JOIN Order o
                ON t.orderRefNumber = o.orderRefNumber
                WHERE t.atrnNumber = :atrnNumber
                AND (:orderRefNumber IS NULL OR t.orderRefNumber = :orderRefNumber)
                AND (:sbiOrderRefNumber IS NULL OR t.sbiOrderRefNumber = :sbiOrderRefNumber)
                AND (:orderAmount IS NULL OR o.orderAmount = :orderAmount)
            """)
    Optional<List<Object[]>> fetchTransactionAndOrderDetail(
            @Param("atrnNumber") String atrnNumber,
            @Param("orderRefNumber") String orderRefNumber,
            @Param("sbiOrderRefNumber") String sbiOrderRefNumber,
            @Param("orderAmount") BigDecimal orderAmount);


@Query(value = "SELECT bm.*, bb.*" +
            "FROM BANK_MASTER_VIEW bm " +
            "JOIN BANK_BRANCHES_VIEW bb ON bm.bankid = bb.bank_id " +
            "WHERE bb.IFSC_CODE = :ifscCode", nativeQuery = true)
    List<Object[]> findBankMasterAndBranchByIfscCode(@Param("ifscCode") String ifscCode);
