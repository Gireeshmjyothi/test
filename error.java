
Parameter 0 of method lockProvider in com.epay.rns.config.shedlockConfig.ShedLockConfig required a bean of type 'javax.sql.DataSource' that could not be found.

	@Configuration
public class ShedLockConfig {
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcLockProvider(dataSource);
    }
}



