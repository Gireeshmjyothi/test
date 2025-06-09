org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sparkController' defined in file [F:\Epay\epay_recon_settlement_service\build\classes\java\main\com\epay\rns\controller\SparkController.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkService' defined in file [F:\Epay\epay_recon_settlement_service\build\classes\java\main\com\epay\rns\service\SparkService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkSession' defined in class path resource [com/epay/rns/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: jakarta/servlet/SingleThreadModel
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
	at com.epay.rns.ReconSettlementServiceApplication.main(ReconSettlementServiceApplication.java:28)
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sparkService' defined in file [F:\Epay\epay_recon_settlement_service\build\classes\java\main\com\epay\rns\service\SparkService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkSession' defined in class path resource [com/epay/rns/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: jakarta/servlet/SingleThreadModel



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

	implementation("org.apache.spark:spark-core_2.13:${apache_spark}") {
		exclude group: 'org.eclipse.jetty'
		exclude group: 'org.eclipse.jetty.aggregate'
		exclude group: 'org.eclipse.jetty.jetty'
	}
	implementation("org.apache.spark:spark-sql_2.13:${apache_spark}") {
		exclude group: 'org.eclipse.jetty'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-core'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-api'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-slf4j-impl'
		exclude group: "org.slf4j"
	}

//	implementation "org.antlr:antlr4-runtime:4.9.3"

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


//	implementation 'com.github.luben:zstd-jni:1.5.7-3'

	compileOnly 'org.projectlombok:lombok'
	compileOnly "org.mapstruct:mapstruct:${mapstruct}"
	compileOnly "org.projectlombok:lombok-mapstruct-binding:${lombok_mapstruct}"

	/*compileOnly "org.apache.spark:spark-core_2.13:${apache_spark}"
	compileOnly "org.apache.spark:spark-sql_2.13:${apache_spark}"*/

	implementation "com.vmware.gemfire:gemfire-core:${gemfire}"
	implementation "com.vmware.gemfire:gemfire-cq:${gemfire}"
	implementation "com.vmware.gemfire:spring-boot-3.3-gemfire-10.1:${gemfire_spring}"

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
			/*"--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED",
			"--add-exports", "java.base/sun.util.calendar=ALL-UNNAMED",
			"--add-exports", "java.base/sun.security.action=ALL-UNNAMED"*/
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


version=0.0.1
spring_boot=3.3.10
dependency_plugin=1.1.7
gorylenko_plugin=2.4.2
swagger=2.6.0
tomcat_jasper=10.1.20
jakarta_jstl=3.0.1
javax_servlet_api=4.0.1
oracle_driver=23.5.0.24.07
apache_spark=4.0.0
jwt=0.12.6
shedlock=5.9.0
mapstruct=1.5.1.Final
aws_s3=2.30.6
epay_logging=0.1.0
epay_cms=0.0.1
gemfire=10.1.2
gemfire_spring=2.0.3
jakarta_persistence=3.1.0
logback_classic=1.5.14
logback_encoder=7.4
lombok_mapstruct=0.2.0
jul_to_slf4j=2.0.9
open_csv=5.11
poi_ooxml=5.4.1


package com.epay.rns.config;

import com.epay.rns.dto.MerchantOrderPayment;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.ByteBuffer;
import java.util.UUID;

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
                .config("spark.ui.enabled", "false") // disable web UI
                .getOrCreate();
    }
    @Bean
    public Encoder<MerchantOrderPayment> getEncoder() {
        return Encoders.bean(MerchantOrderPayment.class);
    }

    public void registerUuidToBytesUdf(SparkSession spark) {
        spark.udf().register("uuidToBytes", (UDF1<String, byte[]>) uuid -> {
            UUID u = UUID.fromString(uuid);
            byte[] buffer = new byte[16];
            ByteBuffer bb = ByteBuffer.wrap(buffer);
            bb.putLong(u.getMostSignificantBits());
            bb.putLong(u.getLeastSignificantBits());
            return buffer;
        }, DataTypes.BinaryType);
    }
}
