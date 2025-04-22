2025-04-22T15:21:56.221+05:30  INFO 15228 --- [demoSpark] [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-04-22T15:21:56.227+05:30  INFO 15228 --- [demoSpark] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-04-22T15:21:56.504+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SparkContext            : Running Spark version 3.5.5
2025-04-22T15:21:56.505+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SparkContext            : OS info Windows 10, 10.0, amd64
2025-04-22T15:21:56.505+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SparkContext            : Java version 21.0.3
2025-04-22T15:21:56.656+05:30  WARN 15228 --- [demoSpark] [           main] org.apache.hadoop.util.Shell             : Did not find winutils.exe: {}

java.io.FileNotFoundException: java.io.FileNotFoundException: HADOOP_HOME and hadoop.home.dir are unset. -see https://wiki.apache.org/hadoop/WindowsProblems
	at org.apache.hadoop.util.Shell.fileNotFoundException(Shell.java:547) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.util.Shell.getHadoopHomeDir(Shell.java:568) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.util.Shell.getQualifiedBin(Shell.java:591) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.util.Shell.<clinit>(Shell.java:688) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.util.StringUtils.<clinit>(StringUtils.java:79) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.conf.Configuration.getBoolean(Configuration.java:1712) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.security.SecurityUtil.setConfigurationInternal(SecurityUtil.java:99) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.security.SecurityUtil.<clinit>(SecurityUtil.java:88) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.security.UserGroupInformation.initialize(UserGroupInformation.java:312) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.security.UserGroupInformation.ensureInitialized(UserGroupInformation.java:300) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.security.UserGroupInformation.getCurrentUser(UserGroupInformation.java:575) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.spark.util.Utils$.$anonfun$getCurrentUserName$1(Utils.scala:2416) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at scala.Option.getOrElse(Option.scala:201) ~[scala-library-2.13.15.jar:na]
	at org.apache.spark.util.Utils$.getCurrentUserName(Utils.scala:2416) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkContext.<init>(SparkContext.scala:329) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkContext$.getOrCreate(SparkContext.scala:2883) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.sql.SparkSession$Builder.$anonfun$getOrCreate$2(SparkSession.scala:1099) ~[spark-sql_2.13-3.5.5.jar:3.5.5]
	at scala.Option.getOrElse(Option.scala:201) ~[scala-library-2.13.15.jar:na]
	at org.apache.spark.sql.SparkSession$Builder.getOrCreate(SparkSession.scala:1093) ~[spark-sql_2.13-3.5.5.jar:3.5.5]
	at com.example.demoSpark.config.SparkConfig.sparkSession(SparkConfig.java:14) ~[main/:na]
	at com.example.demoSpark.config.SparkConfig$$SpringCGLIB$$0.CGLIB$sparkSession$0(<generated>) ~[main/:na]
	at com.example.demoSpark.config.SparkConfig$$SpringCGLIB$$FastClass$$1.invoke(<generated>) ~[main/:na]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:258) ~[spring-core-6.2.5.jar:6.2.5]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:370) ~[spring-context-6.2.5.jar:6.2.5]
	at com.example.demoSpark.config.SparkConfig$$SpringCGLIB$$0.sparkSession(<generated>) ~[main/:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:171) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiateWithFactoryMethod(SimpleInstantiationStrategy.java:88) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:168) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:489) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1361) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1191) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:563) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:347) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.instantiateSingleton(DefaultListableBeanFactory.java:1155) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingleton(DefaultListableBeanFactory.java:1121) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:1056) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:987) ~[spring-context-6.2.5.jar:6.2.5]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:627) ~[spring-context-6.2.5.jar:6.2.5]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:752) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:318) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1361) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1350) ~[spring-boot-3.4.4.jar:3.4.4]
	at com.example.demoSpark.DemoSparkApplication.main(DemoSparkApplication.java:10) ~[main/:na]
Caused by: java.io.FileNotFoundException: HADOOP_HOME and hadoop.home.dir are unset.
	at org.apache.hadoop.util.Shell.checkHadoopHomeInner(Shell.java:467) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.util.Shell.checkHadoopHome(Shell.java:438) ~[hadoop-client-api-3.3.4.jar:na]
	at org.apache.hadoop.util.Shell.<clinit>(Shell.java:515) ~[hadoop-client-api-3.3.4.jar:na]
	... 48 common frames omitted

2025-04-22T15:21:56.671+05:30  WARN 15228 --- [demoSpark] [           main] o.apache.hadoop.util.NativeCodeLoader    : Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
2025-04-22T15:21:56.798+05:30  INFO 15228 --- [demoSpark] [           main] o.apache.spark.resource.ResourceUtils    : ==============================================================
2025-04-22T15:21:56.798+05:30  INFO 15228 --- [demoSpark] [           main] o.apache.spark.resource.ResourceUtils    : No custom resources configured for spark.driver.
2025-04-22T15:21:56.799+05:30  INFO 15228 --- [demoSpark] [           main] o.apache.spark.resource.ResourceUtils    : ==============================================================
2025-04-22T15:21:56.800+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SparkContext            : Submitted application: RnSDemoApp
2025-04-22T15:21:56.836+05:30  INFO 15228 --- [demoSpark] [           main] o.apache.spark.resource.ResourceProfile  : Default ResourceProfile created, executor resources: Map(cores -> name: cores, amount: 1, script: , vendor: , memory -> name: memory, amount: 1024, script: , vendor: , offHeap -> name: offHeap, amount: 0, script: , vendor: ), task resources: Map(cpus -> name: cpus, amount: 1.0)
2025-04-22T15:21:56.844+05:30  INFO 15228 --- [demoSpark] [           main] o.apache.spark.resource.ResourceProfile  : Limiting resource is cpu
2025-04-22T15:21:56.845+05:30  INFO 15228 --- [demoSpark] [           main] o.a.s.resource.ResourceProfileManager    : Added ResourceProfile id: 0
2025-04-22T15:21:56.914+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SecurityManager         : Changing view acls to: V1014352
2025-04-22T15:21:56.914+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SecurityManager         : Changing modify acls to: V1014352
2025-04-22T15:21:56.915+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SecurityManager         : Changing view acls groups to: 
2025-04-22T15:21:56.915+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SecurityManager         : Changing modify acls groups to: 
2025-04-22T15:21:56.916+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SecurityManager         : SecurityManager: authentication disabled; ui acls disabled; users with view permissions: V1014352; groups with view permissions: EMPTY; users with modify permissions: V1014352; groups with modify permissions: EMPTY
2025-04-22T15:21:57.407+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.util.Utils              : Successfully started service 'sparkDriver' on port 63429.
2025-04-22T15:21:57.436+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SparkEnv                : Registering MapOutputTracker
2025-04-22T15:21:57.470+05:30  INFO 15228 --- [demoSpark] [           main] org.apache.spark.SparkEnv                : Registering BlockManagerMaster
2025-04-22T15:21:57.490+05:30  INFO 15228 --- [demoSpark] [           main] o.a.s.s.BlockManagerMasterEndpoint       : Using org.apache.spark.storage.DefaultTopologyMapper for getting topology information
2025-04-22T15:21:57.491+05:30  INFO 15228 --- [demoSpark] [           main] o.a.s.s.BlockManagerMasterEndpoint       : BlockManagerMasterEndpoint up
2025-04-22T15:21:57.493+05:30  WARN 15228 --- [demoSpark] [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sparkSession' defined in class path resource [com/example/demoSpark/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x15bb6bea) cannot access class sun.nio.ch.DirectBuffer (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @0x15bb6bea
2025-04-22T15:21:57.494+05:30  INFO 15228 --- [demoSpark] [           main] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2025-04-22T15:21:57.496+05:30  INFO 15228 --- [demoSpark] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2025-04-22T15:21:57.672+05:30  INFO 15228 --- [demoSpark] [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
2025-04-22T15:21:57.675+05:30  INFO 15228 --- [demoSpark] [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2025-04-22T15:21:57.687+05:30  INFO 15228 --- [demoSpark] [           main] .s.b.a.l.ConditionEvaluationReportLogger : 

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2025-04-22T15:21:57.712+05:30 ERROR 15228 --- [demoSpark] [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sparkSession' defined in class path resource [com/example/demoSpark/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x15bb6bea) cannot access class sun.nio.ch.DirectBuffer (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @0x15bb6bea
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:657) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:489) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1361) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1191) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:563) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:523) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:339) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:347) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:337) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.instantiateSingleton(DefaultListableBeanFactory.java:1155) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingleton(DefaultListableBeanFactory.java:1121) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:1056) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:987) ~[spring-context-6.2.5.jar:6.2.5]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:627) ~[spring-context-6.2.5.jar:6.2.5]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:752) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:439) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:318) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1361) ~[spring-boot-3.4.4.jar:3.4.4]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1350) ~[spring-boot-3.4.4.jar:3.4.4]
	at com.example.demoSpark.DemoSparkApplication.main(DemoSparkApplication.java:10) ~[main/:na]
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x15bb6bea) cannot access class sun.nio.ch.DirectBuffer (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @0x15bb6bea
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:199) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiateWithFactoryMethod(SimpleInstantiationStrategy.java:88) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:168) ~[spring-beans-6.2.5.jar:6.2.5]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-6.2.5.jar:6.2.5]
	... 21 common frames omitted
Caused by: java.lang.IllegalAccessError: class org.apache.spark.storage.StorageUtils$ (in unnamed module @0x15bb6bea) cannot access class sun.nio.ch.DirectBuffer (in module java.base) because module java.base does not export sun.nio.ch to unnamed module @0x15bb6bea
	at org.apache.spark.storage.StorageUtils$.<clinit>(StorageUtils.scala:213) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.storage.BlockManagerMasterEndpoint.<init>(BlockManagerMasterEndpoint.scala:121) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkEnv$.$anonfun$create$9(SparkEnv.scala:353) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkEnv$.registerOrLookupEndpoint$1(SparkEnv.scala:290) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkEnv$.create(SparkEnv.scala:339) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkEnv$.createDriverEnv(SparkEnv.scala:194) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkContext.createSparkEnv(SparkContext.scala:284) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkContext.<init>(SparkContext.scala:478) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.SparkContext$.getOrCreate(SparkContext.scala:2883) ~[spark-core_2.13-3.5.5.jar:3.5.5]
	at org.apache.spark.sql.SparkSession$Builder.$anonfun$getOrCreate$2(SparkSession.scala:1099) ~[spark-sql_2.13-3.5.5.jar:3.5.5]
	at scala.Option.getOrElse(Option.scala:201) ~[scala-library-2.13.15.jar:na]
	at org.apache.spark.sql.SparkSession$Builder.getOrCreate(SparkSession.scala:1093) ~[spark-sql_2.13-3.5.5.jar:3.5.5]
	at com.example.demoSpark.config.SparkConfig.sparkSession(SparkConfig.java:14) ~[main/:na]
	at com.example.demoSpark.config.SparkConfig$$SpringCGLIB$$0.CGLIB$sparkSession$0(<generated>) ~[main/:na]
	at com.example.demoSpark.config.SparkConfig$$SpringCGLIB$$FastClass$$1.invoke(<generated>) ~[main/:na]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:258) ~[spring-core-6.2.5.jar:6.2.5]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:370) ~[spring-context-6.2.5.jar:6.2.5]
	at com.example.demoSpark.config.SparkConfig$$SpringCGLIB$$0.sparkSession(<generated>) ~[main/:na]
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:580) ~[na:na]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.lambda$instantiate$0(SimpleInstantiationStrategy.java:171) ~[spring-beans-6.2.5.jar:6.2.5]
	... 24 common frames omitted
