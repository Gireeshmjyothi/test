
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    public Optional<BankDetailDto> getBankDetailsByIfscCode(String ifsccode) {
        List<Object[]> results = bankRepository.findBankMasterAndBranchByIfscCode(ifsccode);
        
        return results.stream()
                      .findFirst() // Assuming IFSC code returns a single result or the first result is required
                      .map(this::mapToBankDetailDto);
    }

    private BankDetailDto mapToBankDetailDto(Object[] result) {
        // Assuming that bank master fields are in the first half of the array and branch fields in the second half
        BankMasterView bankMasterView = BankMasterView.builder()
                                                      .bankId((String) result[0])
                                                      .bankname((String) result[1])
                                                      // Map other fields as needed
                                                      .build();

        BankBranchView bankBranchView = BankBranchView.builder()
                                                      .branchId((String) result[result.length / 2])
                                                      .bankId((String) result[(result.length / 2) + 1])
                                                      // Map other fields as needed
                                                      .build();

        return new BankDetailDto(bankMasterView, bankBranchView);
    }
}
@Repository
public interface BankRepository extends JpaRepository<BankBranchView, String> {

    @Query(value = "SELECT bb.*, bm.* " +
                   "FROM BANK_BRANCHES_VIEW bb " +
                   "INNER JOIN BANK_MASTER_VIEW bm ON bb.bankId = bm.bankId " +
                   "WHERE bb.ifscCode = :ifsccode", nativeQuery = true)
    List<Object[]> findBankMasterAndBranchByIfscCode(@Param("ifsccode") String ifsccode);
}
