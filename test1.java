Parameter 0 of constructor in com.sbi.epay.notification.thirdpartyservice.EmailClient required a bean of type 'org.springframework.mail.javamail.JavaMailSender' that could not be found.


Action:

Consider defining a bean of type 'org.springframework.mail.javamail.JavaMailSender' in your configuration.  

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@ComponentScan(basePackages = {"org.springframework.boot.context.embedded.tomcat", "com.epay.transaction", "com.sbi.epay"})
@EnableJpaRepositories(basePackages = "com.epay.transaction")
@EntityScan(basePackages = "com.epay.transaction.entity")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
public class EpayTransactionServiceApplication implements WebMvcConfigurer {
