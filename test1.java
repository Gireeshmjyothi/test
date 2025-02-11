Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2025-02-11 18:49:03 [main] ERROR o.s.boot.SpringApplication  - userId= - correlationId= - Application run failed
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'webConfig' defined in URL [jar:file:/F:/Epay/epay_payment_service/libs/logging-service-1.0-SNAPSHOT.jar!/com/sbi/epay/logging/config/WebConfig.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'loggingInterceptor': Lookup method resolution failed
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:795)
	at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:237)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1375)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1212)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:975)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:971)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:625)
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:335)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1363)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1352)
	at com.epay.payment.EpayPaymentServiceApplication.main(EpayPaymentServiceApplication.java:25)
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'loggingInterceptor': Lookup method resolution failed
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.checkLookupMethods(AutowiredAnnotationBeanPostProcessor.java:498)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.determineCandidateConstructors(AutowiredAnnotationBeanPostProcessor.java:368)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.determineConstructorsFromBeanPostProcessors(AbstractAutowireCapableBeanFactory.java:1314)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1209)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200)
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1443)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1353)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:904)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:782)
	... 19 common frames omitted
Caused by: java.lang.IllegalStateException: Failed to introspect Class [com.sbi.epay.logging.utility.LoggingInterceptor] from ClassLoader [jdk.internal.loader.ClassLoaders$AppClassLoader@5a07e868]
	at org.springframework.util.ReflectionUtils.getDeclaredMethods(ReflectionUtils.java:483)
	at org.springframework.util.ReflectionUtils.doWithLocalMethods(ReflectionUtils.java:320)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.checkLookupMethods(AutowiredAnnotationBeanPostProcessor.java:476)
	... 33 common frames omitted
Caused by: java.lang.NoClassDefFoundError: javax/servlet/http/HttpServletRequest
	at java.base/java.lang.Class.getDeclaredMethods0(Native Method)
	at java.base/java.lang.Class.privateGetDeclaredMethods(Class.java:3578)
	at java.base/java.lang.Class.getDeclaredMethods(Class.java:2676)
	at org.springframework.util.ReflectionUtils.getDeclaredMethods(ReflectionUtils.java:465)
	... 35 common frames omitted
Caused by: java.lang.ClassNotFoundException: javax.servlet.http.HttpServletRequest
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:526)
	... 39 common frames omitted
Disconnected from the target VM, address: 'localhost:52339', transport: 'socket'
