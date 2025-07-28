Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sparkSession' defined in class path resource [com/epay/operations/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: jakarta/servlet/SingleThreadModel



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
configurations.configureEach {
	resolutionStrategy {
		force "org.antlr:antlr4-runtime:${antlr4}"
	}
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
	implementation 'org.springframework.boot:spring-boot-starter-batch'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-tomcat'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-jdbc'
	implementation 'org.springframework.kafka:spring-kafka'
	implementation "commons-io:commons-io:${commons_io}"
	implementation "org.springdoc:springdoc-openapi-starter-webmvc-ui:${swagger}"
	implementation "jakarta.persistence:jakarta.persistence-api:${jakarta_persistence}"
	implementation "org.glassfish.web:jakarta.servlet.jsp.jstl:${jakarta_jstl}"
//	implementation "javax.servlet:javax.servlet-api:${javax_servlet_api}"
	implementation "com.oracle.database.jdbc:ojdbc11:${oracle_driver}"
	implementation "com.opencsv:opencsv:${open_csv}"
	implementation "org.apache.poi:poi-ooxml:${poi_ooxml}"
	testImplementation 'org.junit.jupiter:junit-jupiter'
	testImplementation 'org.mockito:mockito-core'
	testImplementation 'org.mockito:mockito-junit-jupiter'

	implementation "com.vmware.gemfire:gemfire-core:${gemfire}"
	implementation "com.vmware.gemfire:gemfire-cq:${gemfire}"
	implementation "com.vmware.gemfire:spring-boot-3.3-gemfire-10.1:${gemfire_spring}"

	implementation("org.apache.spark:spark-core_2.13:4.0.0") /*{
		exclude group: 'org.eclipse.jetty'
		exclude group: 'org.eclipse.jetty.aggregate'
		exclude group: 'org.eclipse.jetty.jetty'
	}*/
		implementation("org.apache.spark:spark-sql_2.13:4.0.0") /*{
		exclude group: 'org.eclipse.jetty'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-core'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-api'
		exclude group: 'org.apache.logging.log4j', module: 'log4j-slf4j-impl'
		exclude group: "org.slf4j"
	}*/


//	implementation("org.apache.hadoop:hadoop-client:3.3.6")


	implementation "software.amazon.awssdk:s3:${aws_s3}"
	implementation 'org.hibernate.orm:hibernate-envers'

	implementation "com.sbi.epay:logging-service:${epay_logging}"
	implementation "name:cache-management-service-${epay_cms}"
	implementation "name:encryption-decryption-service-${epay_enc}"

	implementation "ch.qos.logback:logback-classic:${logback_classic}"
	implementation "net.logstash.logback:logstash-logback-encoder:${logback_encoder}"
	implementation "org.slf4j:jul-to-slf4j:${jul_to_slf4j}"
	implementation "org.slf4j:jcl-over-slf4j:${jul_to_slf4j}"

	compileOnly 'org.projectlombok:lombok'
	compileOnly "org.mapstruct:mapstruct:${mapstruct}"
	compileOnly "org.projectlombok:lombok-mapstruct-binding:${lombok_mapstruct}"

	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation "com.vmware.gemfire:gemfire-core:${gemfire}"
	implementation "com.vmware.gemfire:gemfire-cq:${gemfire}"
	implementation "com.vmware.gemfire:spring-boot-3.3-gemfire-10.1:${gemfire_spring}"
	implementation 'org.liquibase:liquibase-core'

	implementation "net.javacrumbs.shedlock:shedlock-spring:${shedlock}"
	implementation "net.javacrumbs.shedlock:shedlock-provider-jdbc-template:${shedlock}"
	runtimeOnly "org.antlr:antlr4-runtime:${antlr4}"

	implementation("com.jcraft:jsch:${jsch}")

	implementation "org.apache.sshd:sshd-core:${sftp}"
	implementation "org.apache.sshd:sshd-sftp:${sftp}"

	implementation "net.javacrumbs.shedlock:shedlock-spring:${shedlock}"
	implementation "net.javacrumbs.shedlock:shedlock-provider-jdbc-template:${shedlock}"

	annotationProcessor 'org.projectlombok:lombok'
	annotationProcessor "org.mapstruct:mapstruct-processor:${mapstruct}"

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
	useJUnitPlatform()
}

/*bootRun {
	jvmArgs += [
			"--add-exports", "java.base/sun.util.calendar=ALL-UNNAMED",
			"--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED",
			"--add-exports", "java.base/sun.security.action=ALL-UNNAMED"
	]
}*/

/*tasks.withType(JavaExec).configureEach {
	jvmArgs += [
			"--add-exports", "java.base/sun.util.calendar=ALL-UNNAMED",
			"--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED",
			"--add-exports", "java.base/sun.security.action=ALL-UNNAMED"
	]
}*/

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

//java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -jar build/libs/your-app.jar
