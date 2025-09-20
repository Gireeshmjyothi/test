plugins {
    id 'java'
    id 'application'
}

group = 'com.epay'
version = "${version_recon_spark_job}"
sourceCompatibility = JavaLanguageVersion.of(21)
targetCompatibility = JavaLanguageVersion.of(21)
ext {
    mainMethodClass = 'com.epay.operations.recon.ReconSparkAppMain'
    destDir = 'libs'
}
application {
    mainClass = "${mainMethodClass}"
}

tasks.register('fatJar', Jar) {
    manifest {
        attributes 'Main-Class': "${mainMethodClass}"
    }
    archiveClassifier = ''
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    zip64 true
    from {
        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
    }
    with jar
    destinationDirectory = file("${rootProject.projectDir}/${destDir}")
    exclude 'META-INF/*.SF'
    exclude 'META-INF/*.DSA'
    exclude 'META-INF/*.RSA'
}

build.finalizedBy(fatJar)

repositories {
    mavenCentral()
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
}

configurations.configureEach {
    exclude group: 'log4j', module: 'log4j'
}

dependencies {
    // Spring Core
    implementation "org.springframework:spring-context:${spring}"
    implementation "org.springframework:spring-jdbc:${spring}"
    implementation "com.oracle.database.jdbc:ojdbc11:${oracle_driver}"
    implementation "org.springframework:spring-webflux:${web_flux}"
    implementation "org.springframework:spring-web:${web_flux}"
    implementation "io.projectreactor.netty:reactor-netty-http:1.2.10"

    implementation "com.sbi.epay:logging-service:${epay_logging}"
    implementation "ch.qos.logback:logback-classic:${logback_classic}"


    implementation "org.apache.spark:spark-core_2.13:${apache_spark}"

    //Excel
    implementation("com.crealytics:spark-excel_2.13:${spark_excel}")

    //Hadoop-aws
    implementation("org.apache.hadoop:hadoop-aws:${hadoop_aws}")

    //AWS SDK
    implementation("com.amazonaws:aws-java-sdk-bundle:${aws_java_sdk}")

    // Lombok
    compileOnly "org.projectlombok:lombok:${lombok}"
    compileOnly "org.mapstruct:mapstruct:${mapstruct}"
    compileOnly "org.apache.spark:spark-sql_2.13:${apache_spark}"

    annotationProcessor "org.projectlombok:lombok:${lombok}"
    annotationProcessor "org.mapstruct:mapstruct-processor:${mapstruct}"


}

test {
    useJUnitPlatform()
}
