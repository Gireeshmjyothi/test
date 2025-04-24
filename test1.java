plugins {
    id 'java'
    id 'war'
    id 'org.springframework.boot' version '3.3.10'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.rajput'
version = '0.0.1'
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}
configurations.configureEach {
    exclude group: 'org.apache.logging.log4j', module: 'log4j-to-slf4j'
    exclude group: 'ch.qos.logback', module: 'logback-classic'
    exclude group: 'ch.qos.logback', module: 'logback-core'
}
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'

    implementation 'org.apache.tomcat.embed:tomcat-embed-jasper:10.1.20'
    implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.1'
    implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1'

    implementation "com.oracle.database.jdbc:ojdbc11:23.5.0.24.07"

    implementation ('org.apache.spark:spark-core_2.13:3.5.1') {
        exclude group: 'org.eclipse.jetty'
        exclude group: 'org.eclipse.jetty.aggregate'
        exclude group: 'org.eclipse.jetty.jetty'
        exclude group: 'javax.servlet'
        exclude group: 'jakarta.servlet'
    }
    implementation ('org.apache.spark:spark-sql_2.13:3.5.1') {
        exclude group: 'org.eclipse.jetty'
        exclude group: 'javax.servlet'
        exclude group: 'jakarta.servlet'
    }

    implementation 'org.apache.logging.log4j:log4j-api:2.20.0'
    implementation 'org.apache.logging.log4j:log4j-core:2.20.0'
    implementation 'org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0'

    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'

    implementation 'org.apache.logging.log4j:log4j-api:2.20.0'
    implementation 'org.apache.logging.log4j:log4j-core:2.20.0'
    implementation 'org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0'

    implementation 'javax.servlet:javax.servlet-api:4.0.1'
}

tasks.named('test') {
    useJUnitPlatform()
}

bootRun {
    jvmArgs += [
        "--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED"
    ]
}
war {

}
//bootWar {
//    archiveFileName = 'springboot-jsp-spark-demo.war'
//}
//java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -jar build/libs/your-app.war
