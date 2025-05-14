
Parameter 0 of method lockProvider in com.epay.rns.config.shedlockConfig.ShedLockConfig required a bean of type 'javax.sql.DataSource' that could not be found.

	@Configuration
public class ShedLockConfig {
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcLockProvider(dataSource);
    }
}

spring:
  datasource:
    url: jdbc:oracle:thin:@11.133.167.154:2600:dbdev1
    username: Tests
    password: Tests
    driver-class-name: oracle.jdbc.OracleDriver



