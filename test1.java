
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
@Repository
public interface BankRepository extends JpaRepository<BankBranchView, String> {

    @Query(value = "SELECT bm.*, bb.* " +
                   "FROM BANK_MASTER_VIEW bm " +
                   "JOIN BANK_BRANCHES_VIEW bb ON bm.bankId = bb.bankId " +
                   "WHERE bb.ifscCode = :ifsccode", nativeQuery = true)
    Object[] findBankMasterAndBranchByIfscCode(@Param("ifsccode") String ifsccode);
}
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    public Optional<BankDetailDto> getBankDetailsByIfscCode(String ifsccode) {
        return Optional.ofNullable(bankRepository.findBankMasterAndBranchByIfscCode(ifsccode))
                       .map(this::mapToBankDetailDto);
    }

    private BankDetailDto mapToBankDetailDto(Object[] result) {
        BankMasterView bankMasterView = mapToBankMasterView(result);
        BankBranchView bankBranchView = mapToBankBranchView(result);
        return new BankDetailDto(bankMasterView, bankBranchView);
    }

    private BankMasterView mapToBankMasterView(Object[] result) {
        return Stream.of(new BankMasterView())
                     .peek(bm -> bm.setBankId((String) result[0]))
                     .peek(bm -> bm.setBankname((String) result[1]))
                     // Map other fields as needed
                     .findFirst()
                     .orElse(null);
    }

    private BankBranchView mapToBankBranchView(Object[] result) {
        return Stream.of(new BankBranchView())
                     .peek(bb -> bb.setBranchId((String) result[8])) // Adjust index based on query result
                     .peek(bb -> bb.setBankId((String) result[9]))
                     // Map other fields as needed
                     .findFirst()
                     .orElse(null);
    }
}
