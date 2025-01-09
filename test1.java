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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankMasterViewService {

    private final BankMasterViewRepository bankMasterViewRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public BankMasterViewService(BankMasterViewRepository bankMasterViewRepository, ObjectMapper objectMapper) {
        this.bankMasterViewRepository = bankMasterViewRepository;
        this.objectMapper = objectMapper;
    }

    public List<BankMasterViewDto> getAllActiveBankDtos() {
        List<BankMasterView> activeBanks = bankMasterViewRepository.findAllActiveBanks();

        // Convert list of entities to list of DTOs
        return activeBanks.stream()
                          .map(bank -> objectMapper.convertValue(bank, BankMasterViewDto.class))
                          .collect(Collectors.toList());
    }
}
