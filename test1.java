plugins {
	id 'java'
	id 'org.springframework.boot' version '3.4.4'
	id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'

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

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-web'
	//lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

	//jpa
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

	//oracle db
	implementation "com.oracle.database.jdbc:ojdbc11:23.5.0.24.07"

	//spark
	implementation 'org.apache.spark:spark-core_2.12:3.5.0'
	implementation 'org.apache.spark:spark-sql_2.12:3.5.0'

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
	useJUnitPlatform()
}


3 actionable tasks: 1 executed, 2 up-to-date
SLF4J(W): Class path contains multiple SLF4J providers.
SLF4J(W): Found provider [org.apache.logging.slf4j.SLF4JServiceProvider@3b6eb2ec]
SLF4J(W): Found provider [ch.qos.logback.classic.spi.LogbackServiceProvider@1e643faf]
SLF4J(W): See https://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J(I): Actual provider is of type [org.apache.logging.slf4j.SLF4JServiceProvider@3b6eb2ec]
Exception in thread "main" java.lang.ExceptionInInitializerError
	at com.example.demoSpark.DemoSparkApplication.main(DemoSparkApplication.java:10)
Caused by: org.apache.logging.log4j.LoggingException: log4j-slf4j2-impl cannot be present with log4j-to-slf4j
	at org.apache.logging.slf4j.Log4jLoggerFactory.validateContext(Log4jLoggerFactory.java:67)
	at org.apache.logging.slf4j.Log4jLoggerFactory.newLogger(Log4jLoggerFactory.java:49)
	at org.apache.logging.slf4j.Log4jLoggerFactory.newLogger(Log4jLoggerFactory.java:32)
	at org.apache.logging.log4j.spi.AbstractLoggerAdapter.getLogger(AbstractLoggerAdapter.java:52)
	at org.apache.logging.slf4j.Log4jLoggerFactory.getLogger(Log4jLoggerFactory.java:32)
	at org.slf4j.LoggerFactory.getLogger(LoggerFactory.java:447)
	at org.apache.commons.logging.impl.SLF4JLogFactory.getInstance(SLF4JLogFactory.java:155)
	at org.apache.commons.logging.impl.SLF4JLogFactory.getInstance(SLF4JLogFactory.java:132)
	at org.apache.commons.logging.LogFactory.getLog(LogFactory.java:273)
	at org.springframework.boot.SpringApplication.<clinit>(SpringApplication.java:202)
	... 1 more

FAILURE: Build failed with an exception.
