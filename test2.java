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
	exclude group: 'org.apache.logging.log4j', module: 'log4j-to-slf4j'
	exclude group: 'ch.qos.logback', module: 'logback-classic'
	exclude group: 'ch.qos.logback', module: 'logback-core'
}

repositories {
	mavenCentral()
	flatDir {
		dirs "libs"
	}
	maven {
		url "https://gitlab.epay.sbi/api/v4/projects/48/packages/maven"
		credentials(PasswordCredentials) {
			username = project.findProperty("gitlab.username")?: System.getenv("CI_USERNAME")
			password = project.findProperty("gitlab.token")?: System.getenv("CI_JOB_TOKEN")
		}
		authentication {
			basic(BasicAuthentication)
		}
	}
}

dependencies {
	implementation ('org.springframework.boot:spring-boot-starter-web') {
		exclude group: 'org.slf4j', module: 'slf4j-log4j12'
	}
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.boot:spring-boot-starter-tomcat'
	implementation "org.springdoc:springdoc-openapi-starter-webmvc-ui:${swagger}"
	implementation "org.apache.tomcat.embed:tomcat-embed-jasper:${tomcat_jasper}"
	implementation "jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:${jakarta_jstl}"
	implementation "org.glassfish.web:jakarta.servlet.jsp.jstl:${jakarta_jstl}"
	implementation "javax.servlet:javax.servlet-api:${javax_servlet_api}"

	implementation "com.oracle.database.jdbc:ojdbc11:${oracle_driver}"

	//Spark dependency
	implementation("org.apache.spark:spark-core_2.13:${apache_spark}") {
		exclude group: 'org.eclipse.jetty'
		exclude group: 'org.eclipse.jetty.aggregate'
		exclude group: 'org.eclipse.jetty.jetty'
	}
	implementation("org.apache.spark:spark-sql_2.13:${apache_spark}") {
		exclude group: 'org.eclipse.jetty'
	}
	implementation "software.amazon.awssdk:s3:${aws_s3}"

	implementation "name:logging-service-${epay_logging}"

	//kafka
	implementation 'org.springframework.kafka:spring-kafka'

	//lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

	//mapstruct
	implementation "org.mapstruct:mapstruct:${mapstruct}"
	annotationProcessor "org.mapstruct:mapstruct-processor:${mapstruct}"

	//gemfire
	implementation "com.vmware.gemfire:gemfire-core:${gemfire}"
	implementation "com.vmware.gemfire:gemfire-cq:${gemfire}"
	implementation "com.vmware.gemfire:spring-boot-3.3-gemfire-10.1:${gemfire_spring}"

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
	useJUnitPlatform()
}
bootRun {
	jvmArgs += [
			"--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED"
	]
}

springBoot  {
	buildInfo()
}

gitProperties {
	failOnNoGitDirectory = false // Avoid build failure if .git directory is missing
	keys = ['git.branch', 'git.commit.id', 'git.commit.time', 'git.commit.id.abbrev']
}
//java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -jar build/libs/your-app.war
