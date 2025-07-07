implementation('com.crealytics:spark-excel_2.12:3.5.0_0.20.3') {
    exclude group: 'org.slf4j', module: 'slf4j-log4j12'
    exclude group: 'log4j', module: 'log4j'
    exclude group: 'org.apache.logging.log4j'
    exclude group: 'ch.qos.logback'
    exclude group: 'com.fasterxml.jackson.core'
    exclude group: 'com.fasterxml.jackson.module'
}
