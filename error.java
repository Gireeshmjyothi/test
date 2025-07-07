
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'gemfireCache': FactoryBean threw exception on object creation
	at org.springframework.beans.factory.support.FactoryBeanRegistrySupport.doGetObjectFromFactoryBean(FactoryBeanRegistrySupport.java:188)
	at org.springframework.beans.factory.support.FactoryBeanRegistrySupport.getObjectFromFactoryBean(FactoryBeanRegistrySupport.java:124)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getObjectForBeanInstance(AbstractBeanFactory.java:1867)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.getObjectForBeanInstance(AbstractAutowireCapableBeanFactory.java:1296)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:259)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200)
	at org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference(BeanDefinitionValueResolver.java:365)
	... 19 common frames omitted


implementation("com.crealytics:spark-excel_2.12:3.5.0_0.20.3")
