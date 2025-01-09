
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
