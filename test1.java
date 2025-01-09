@Data
@Builder
public class BankMasterViewDto {
    private String bankId;
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

 @Query("SELECT b FROM BankMasterView b WHERE b.status = 'A'")
    List<BankMasterView> findAllActiveBanks();
