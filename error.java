Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2025-06-11 14:52:29.366 ERROR | com.epay.rns.dao.FileConfigDao:859 | principal=  | scenario= | operation= | correlation= | reportFailure | Application run failed
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'reconFileDtlsDao' defined in file [F:\Epay\epay_recon_settlement_service\build\classes\java\main\com\epay\rns\dao\ReconFileDtlsDao.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'reconFileDtlsRepository' defined in com.epay.rns.repository.ReconFileDtlsRepository defined in @EnableJpaRepositories declared on ReconSettlementServiceApplication: null
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
Disconnected from the target VM, address: 'localhost:63899', transport: 'socket'
	at com.epay.rns.ReconSettlementServiceApplication.main(ReconSettlementServiceApplication.java:32)
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'reconFileDtlsRepository' defined in com.epay.rns.repository.ReconFileDtlsRepository defined in @EnableJpaRepositories declared on ReconSettlementServiceApplication: null
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1806)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:600)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:337)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:335)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200)
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1448)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1358)
	at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:904)
	at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:782)
	... 19 common frames omitted
Caused by: java.lang.ExceptionInInitializerError: null
	at org.springframework.data.jpa.repository.query.HqlQueryParser.parseQuery(HqlQueryParser.java:48)
	at org.springframework.data.jpa.repository.query.HqlQueryParser.parse(HqlQueryParser.java:63)
	at org.springframework.data.jpa.repository.query.JpaQueryParserSupport$ParseState.lambda$0(JpaQueryParserSupport.java:182)
	at org.springframework.data.util.Lazy.getNullable(Lazy.java:135)
	at org.springframework.data.util.Lazy.get(Lazy.java:113)
	at org.springframework.data.jpa.repository.query.JpaQueryParserSupport$ParseState.getContext(JpaQueryParserSupport.java:194)
	at org.springframework.data.jpa.repository.query.JpaQueryParserSupport.findAlias(JpaQueryParserSupport.java:96)
	at org.springframework.data.jpa.repository.query.JpaQueryEnhancer.detectAlias(JpaQueryEnhancer.java:124)
	at org.springframework.data.jpa.repository.query.StringQuery.<init>(StringQuery.java:91)
	at org.springframework.data.jpa.repository.query.DeclaredQuery.of(DeclaredQuery.java:40)
	at org.springframework.data.jpa.repository.query.JpaQueryMethod.assertParameterNamesInAnnotatedQuery(JpaQueryMethod.java:168)
	at org.springframework.data.jpa.repository.query.JpaQueryMethod.<init>(JpaQueryMethod.java:149)
	at org.springframework.data.jpa.repository.query.DefaultJpaQueryMethodFactory.build(DefaultJpaQueryMethodFactory.java:44)
	at org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy$AbstractQueryLookupStrategy.resolveQuery(JpaQueryLookupStrategy.java:94)
	at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.lookupQuery(QueryExecutorMethodInterceptor.java:115)
	at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.mapMethodsToQuery(QueryExecutorMethodInterceptor.java:103)
	at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.lambda$new$0(QueryExecutorMethodInterceptor.java:92)
	at java.base/java.util.Optional.map(Optional.java:260)
	at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.<init>(QueryExecutorMethodInterceptor.java:92)
	at org.springframework.data.repository.core.support.RepositoryFactorySupport.getRepository(RepositoryFactorySupport.java:357)
	at org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport.lambda$afterPropertiesSet$5(RepositoryFactoryBeanSupport.java:290)
	at org.springframework.data.util.Lazy.getNullable(Lazy.java:135)
	at org.springframework.data.util.Lazy.get(Lazy.java:113)
	at org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport.afterPropertiesSet(RepositoryFactoryBeanSupport.java:296)
	at org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean.afterPropertiesSet(JpaRepositoryFactoryBean.java:132)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.invokeInitMethods(AbstractAutowireCapableBeanFactory.java:1853)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1802)
	... 30 common frames omitted
Caused by: java.lang.UnsupportedOperationException: java.io.InvalidClassException: org.antlr.v4.runtime.atn.ATN; Could not deserialize ATN with version 4 (expected 3).
	at org.antlr.v4.runtime.atn.ATNDeserializer.deserialize(ATNDeserializer.java:187)
	at org.springframework.data.jpa.repository.query.HqlLexer.<clinit>(HqlLexer.java:1345)
	... 57 common frames omitted
Caused by: java.io.InvalidClassException: org.antlr.v4.runtime.atn.ATN; Could not deserialize ATN with version 4 (expected 3).
	... 59 common frames omitted

> Task :com.epay.rns.ReconSettlementServiceApplication.main() FAILED




plugins {
	id 'java'
	id 'org.springframework.boot' version "${spring_boot}"
	id 'io.spring.dependency-management' version "${dependency_plugin}"
	id 'com.gorylenko.gradle-git-properties' version "${gorylenko_plugin}"
}

group = 'com.epay'
version = "${version}"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom annotationProcessor
	}
}
configurations.configureEach {
	exclude group: 'org.springframework.boot', module: 'spring-boot-starter-logging'
}

repositories {
	maven {//To download gemfire dependencies
		url "https://gitlab.epay.sbi/api/v4/projects/48/packages/maven"
		credentials(PasswordCredentials) {
			username = project.findProperty("gitlab.username") ?: System.getenv("CI_USERNAME")
			password = project.findProperty("gitlab.token") ?: System.getenv("CI_JOB_TOKEN")
		}
		authentication {
			basic(BasicAuthentication)
		}
	}
	maven {//To download ePay2.0 utilities dependencies
		url "https://gitlab.epay.sbi/api/v4/projects/16/packages/maven"
		credentials(PasswordCredentials) {
			username = project.findProperty("gitlab.username")?: System.getenv("CI_USERNAME")
			password = project.findProperty("gitlab.token")?: System.getenv("CI_JOB_TOKEN")
		}
		authentication {
			basic(BasicAuthentication)
		}
	}
	mavenCentral()
	flatDir {
		dirs "libs"
	}
}
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-tomcat'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation "org.springdoc:springdoc-openapi-starter-webmvc-ui:${swagger}"
	implementation 'org.springframework.kafka:spring-kafka'
	implementation "org.apache.tomcat.embed:tomcat-embed-jasper:${tomcat_jasper}"
    implementation "jakarta.persistence:jakarta.persistence-api:${jakarta_persistence}"
	implementation "jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:${jakarta_jstl}"
	implementation "org.glassfish.web:jakarta.servlet.jsp.jstl:${jakarta_jstl}"
	implementation "javax.servlet:javax.servlet-api:${javax_servlet_api}"
	implementation "com.oracle.database.jdbc:ojdbc11:${oracle_driver}"
	implementation("com.opencsv:opencsv:${open_csv}")
	implementation("org.apache.poi:poi-ooxml:${poi_ooxml}")
	implementation 'org.liquibase:liquibase-core'

	implementation("org.apache.spark:spark-core_2.12:${apache_spark}") {
		exclude group: 'org.eclipse.jetty'
		exclude group: 'org.eclipse.jetty.aggregate'
		exclude group: 'org.eclipse.jetty.jetty'
	}
	implementation("org.apache.spark:spark-sql_2.12:${apache_spark}") {
		exclude group: 'org.eclipse.jetty'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-core'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-api'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-slf4j-impl'
		exclude group: "org.slf4j"
	}

	implementation "org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.5"

	implementation "org.antlr:antlr4-runtime:4.9.3"
	implementation "software.amazon.awssdk:s3:${aws_s3}"
	implementation 'org.hibernate.orm:hibernate-envers'

	implementation "com.sbi.epay:logging-service:${epay_logging}"
	implementation "name:cache-management-service-${epay_cms}"

	implementation "ch.qos.logback:logback-classic:${logback_classic}"
	implementation "net.logstash.logback:logstash-logback-encoder:${logback_encoder}"
	implementation "org.slf4j:jul-to-slf4j:${jul_to_slf4j}"
	implementation "org.slf4j:jcl-over-slf4j:${jul_to_slf4j}"

	implementation "net.javacrumbs.shedlock:shedlock-spring:${shedlock}"
	implementation "net.javacrumbs.shedlock:shedlock-provider-jdbc:${shedlock}"


	implementation 'com.github.luben:zstd-jni:1.5.7-3'

	compileOnly 'org.projectlombok:lombok'
	compileOnly "org.mapstruct:mapstruct:${mapstruct}"
	compileOnly "org.projectlombok:lombok-mapstruct-binding:${lombok_mapstruct}"

	implementation "com.vmware.gemfire:gemfire-core:${gemfire}"
	implementation "com.vmware.gemfire:gemfire-cq:${gemfire}"
	implementation "com.vmware.gemfire:spring-boot-3.3-gemfire-10.1:${gemfire_spring}"
    implementation 'org.liquibase:liquibase-core'

	annotationProcessor 'org.projectlombok:lombok'
	annotationProcessor "org.mapstruct:mapstruct-processor:${mapstruct}"

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
	useJUnitPlatform()
}


tasks.withType(JavaExec).configureEach {
	jvmArgs += [
			"--add-exports", "java.base/sun.util.calendar=ALL-UNNAMED",
            "--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED",
			"--add-exports", "java.base/sun.security.action=ALL-UNNAMED"
	]
}

springBoot  {
	buildInfo()
}
tasks.named('bootJar') {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

gitProperties {
	failOnNoGitDirectory = false // Avoid build failure if .git directory is missing
	keys = ['git.branch', 'git.commit.id', 'git.commit.time', 'git.commit.id.abbrev']
}

configurations.all{
	resolutionStrategy {
		force 'org.antlr:antlr4-runtime:4.9.3'
	}
}
//java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -jar build/libs/your-app.war
