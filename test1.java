@Configuration
public class SparkConfig {
    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.master}")
    private String master;
    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName(appName)
                .master(master)
                .config("spark.ui.enabled", "true") // disable web UI
                .getOrCreate();
    }
    @Bean
    public Encoder<MerchantOrderPayment> myMerchantEncoder() {
        return Encoders.bean(MerchantOrderPayment.class);
    }
}


 ERROR org.springframework.boot.SpringApplication - Application run failed
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sparkController' defined in file [C:\Users\v1014352\Downloads\springboot-jsp-spark-demo\springboot-jsp-spark-demo\build\classes\java\main\com\rajput\controller\SparkController.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkService' defined in file [C:\Users\v1014352\Downloads\springboot-jsp-spark-demo\springboot-jsp-spark-demo\build\classes\java\main\com\rajput\service\SparkService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkSession' defined in class path resource [com/rajput/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:795) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:237) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1212) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:975) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:971) ~[spring-context-6.1.18.jar:6.1.18]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625) ~[spring-context-6.1.18.jar:6.1.18]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.3.10.jar:3.3.10]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754) [spring-boot-3.3.10.jar:3.3.10]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456) [spring-boot-3.3.10.jar:3.3.10]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:335) [spring-boot-3.3.10.jar:3.3.10]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1363) [spring-boot-3.3.10.jar:3.3.10]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1352) [spring-boot-3.3.10.jar:3.3.10]
	at com.rajput.SpringbootJspSparkDemoApplication.main(SpringbootJspSparkDemoApplication.java:9) [main/:?]
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sparkService' defined in file [C:\Users\v1014352\Downloads\springboot-jsp-spark-demo\springboot-jsp-spark-demo\build\classes\java\main\com\rajput\service\SparkService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkSession' defined in class path resource [com/rajput/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:795) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:237) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1212) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1448) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1358) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:904) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:782) ~[spring-beans-6.1.18.jar:6.1.18]
	... 19 more
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sparkSession' defined in class path resource [com/rajput/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:648) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:485) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1355) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1185) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1448) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1358) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:904) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:782) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:237) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1212) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337) ~[spring-beans-6.1.18.jar:6.1.18]
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sparkService' defined in file [C:\Users\v1014352\Downloads\springboot-jsp-spark-demo\springboot-jsp-spark-demo\build\classes\java\main\com\rajput\service\SparkService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkSession' defined in class path resource [com/rajput/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet

	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1448) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1358) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:904) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:782) ~[spring-beans-6.1.18.jar:6.1.18]
	... 19 more
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:178) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:644) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:485) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1355) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1185) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1448) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1358) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:904) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:782) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:237) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1375) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1212) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1448) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1358) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:904) ~[spring-beans-6.1.18.jar:6.1.18]
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:782) ~[spring-beans-6.1.18.jar:6.1.18]
	... 19 more
Caused by: javax.servlet.UnavailableException: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sparkSession' defined in class path resource [com/rajput/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet
