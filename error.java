// Required by Apache Geode / VMware GemFire
implementation 'org.apache.logging.log4j:log4j-api:2.20.0'
implementation 'org.apache.logging.log4j:log4j-core:2.20.0'

// Optional: If you want SLF4J logging to go to Log4j2
implementation 'org.apache.logging.log4j:log4j-slf4j-impl:2.20.0'

// Exclude Spring Boot's default logger (Logback)
configurations {
    all {
        exclude group: 'ch.qos.logback', module: 'logback-classic'
    }
}
