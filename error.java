implementation 'org.apache.logging.log4j:log4j-api:2.20.0'
implementation 'org.apache.logging.log4j:log4j-core:2.20.0'

implementation('com.crealytics:spark-excel_2.12:3.5.0_0.20.3') {
    exclude group: 'org.slf4j', module: 'slf4j-log4j12'
    exclude group: 'log4j', module: 'log4j'
    exclude group: 'org.apache.logging.log4j'
    exclude group: 'ch.qos.logback'
    exclude group: 'org.slf4j', module: 'jul-to-slf4j'
}


bootRun {
    jvmArgs += ['-Dlog4j2.disableThreadContextStack=true']
}


implementation "ch.qos.logback:logback-classic:${logback_classic}"
implementation "net.logstash.logback:logstash-logback-encoder:${logback_encoder}"

// Only for GemFire
implementation 'org.apache.logging.log4j:log4j-api:2.20.0'
implementation 'org.apache.logging.log4j:log4j-core:2.20.0'
