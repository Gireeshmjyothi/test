@Repository
@RequiredArgsConstructor
public class ErrorLogDao {
    private static final LoggerUtility logger = LoggerFactoryUtility.getLogger(ErrorLogDao.class);

    private final ErrorLogRepository errorLogRepository;
    private final ObjectMapper objectMapper;

    public void saveErrorLog(ErrorLogDto errorLogDto) {
        logger.info("Saving Error Log : {}", errorLogDto);
        errorLogRepository.save(objectMapper.convertValue(errorLogDto, ErrorLog.class));
        logger.info("Error Log Saved Successfully.");
    }
}


@Data
@Builder
public class ErrorLogDto {
    private String mID;
    private String orderRefNumber;
    private String sbiOrderRefNumber;
    private String atrn;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Enumerated(EnumType.STRING)
    private PayMode payMode;

    @Enumerated(EnumType.STRING)
    private FailureReason failureReason;

    private String errorCode;
    private String errorMessage;
}

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder
@Entity
@Table(name = "ERROR_LOG")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorLog extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id;

    private String mID;
    private String orderRefNumber;
    private String sbiOrderRefNumber;
    private String atrn;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    @Enumerated(EnumType.STRING)
    private PayMode payMode;

    @Enumerated(EnumType.STRING)
    private FailureReason failureReason;

    private String errorCode;
    private String errorMessage;
}
