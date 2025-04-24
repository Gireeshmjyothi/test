dependencies {
    // Spring Boot Web + Tomcat (provided as runtime)
    implementation 'org.springframework.boot:spring-boot-starter-web'
    providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'

    // JSP support
    implementation 'org.apache.tomcat.embed:tomcat-embed-jasper:10.1.20'
    implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.1'
    implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1'

    // Oracle JDBC Driver
    implementation 'com.oracle.database.jdbc:ojdbc11:23.5.0.24.07' // Latest as of April 2025

    // Apache Spark Core & SQL - Scala 2.13 compatible with Java 21
    implementation('org.apache.spark:spark-core_2.13:3.5.1') {
        exclude group: 'org.eclipse.jetty'
        exclude group: 'javax.servlet'
        exclude group: 'jakarta.servlet'
    }
    implementation('org.apache.spark:spark-sql_2.13:3.5.1') {
        exclude group: 'org.eclipse.jetty'
        exclude group: 'javax.servlet'
        exclude group: 'jakarta.servlet'
    }

    // Logging - Log4j2 (Spring Boot 3 uses slf4j 2.x under the hood)
    implementation 'org.apache.logging.log4j:log4j-api:2.22.1'
    implementation 'org.apache.logging.log4j:log4j-core:2.22.1'
    implementation 'org.apache.logging.log4j:log4j-slf4j2-impl:2.22.1'

    // Servlet API - only needed for compile, already included in runtime
    compileOnly 'javax.servlet:javax.servlet-api:4.0.1'

    // Lombok
    compileOnly 'org.projectlombok:lombok:1.18.30'
    annotationProcessor 'org.projectlombok:lombok:1.18.30'

    // Testing
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
        }
