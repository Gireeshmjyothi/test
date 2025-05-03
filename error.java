application with 'debug' enabled.
18:10:53.565 [main] ERROR org.springframework.boot.SpringApplication - Application run failed
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sparkController' defined in file [C:\Users\v1014352\Downloads\springboot-jsp-spark-demo 2\springboot-jsp-spark-demo\build\classes\java\main\com\rajput\controller\SparkController.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkService' defined in file [C:\Users\v1014352\Downloads\springboot-jsp-spark-demo 2\springboot-jsp-spark-demo\build\classes\java\main\com\rajput\service\SparkService.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkSession' defined in class path resource [com/rajput/config/SparkConfig.class]: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: Servlet class org.glassfish.jersey.servlet.ServletContainer is not a javax.servlet.Servlet

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
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

    implementation 'org.apache.tomcat.embed:tomcat-embed-jasper:10.1.20'
    implementation 'jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.1'
    implementation 'org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1'

    implementation "com.oracle.database.jdbc:ojdbc11:23.5.0.24.07"
    implementation 'org.antlr:antlr4-runtime:4.9.3'

    implementation "javax.persistence:javax.persistence-api:2.2"
    implementation 'com.opencsv:opencsv:5.9'

    //Spark-core dependency
    implementation('org.apache.spark:spark-core_2.12:3.5.5') {
        exclude group: 'org.eclipse.jetty'
        exclude group: 'javax.servlet'
    }
    implementation('org.apache.spark:spark-sql_2.12:3.5.5') {
        exclude group: 'org.eclipse.jetty'
        exclude group: 'javax.servlet'
    }
    //Spark-excel dependency
    implementation 'com.crealytics:spark-excel_2.12:3.5.0_0.20.3'

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



    //mapStruct
    implementation 'org.mapstruct:mapstruct:1.5.2.Final'
    annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.2.Final'
}

tasks.named('test') {
    useJUnitPlatform()
}

bootRun {
    jvmArgs = [
            "--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED"
    ]
}
war {

}
//bootWar {
//    archiveFileName = 'springboot-jsp-spark-demo.war'
//}
//java --add-exports=java.base/sun.nio.ch=ALL-UNNAMED -jar build/libs/your-app.war
