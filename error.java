Task :dependencies

------------------------------------------------------------
Root project 'epay_recon_settlement_service'
------------------------------------------------------------

runtimeClasspath - Runtime classpath of source set 'main'.
+--- org.springframework.boot:spring-boot-starter-web -> 3.3.10
|    +--- org.springframework.boot:spring-boot-starter:3.3.10
|    |    +--- org.springframework.boot:spring-boot:3.3.10
|    |    |    +--- org.springframework:spring-core:6.1.18
|    |    |    |    \--- org.springframework:spring-jcl:6.1.18
|    |    |    \--- org.springframework:spring-context:6.1.18
|    |    |         +--- org.springframework:spring-aop:6.1.18
|    |    |         |    +--- org.springframework:spring-beans:6.1.18
|    |    |         |    |    \--- org.springframework:spring-core:6.1.18 (*)
|    |    |         |    \--- org.springframework:spring-core:6.1.18 (*)
|    |    |         +--- org.springframework:spring-beans:6.1.18 (*)
|    |    |         +--- org.springframework:spring-core:6.1.18 (*)
|    |    |         +--- org.springframework:spring-expression:6.1.18
|    |    |         |    \--- org.springframework:spring-core:6.1.18 (*)
|    |    |         \--- io.micrometer:micrometer-observation:1.12.12 -> 1.13.12
|    |    |              \--- io.micrometer:micrometer-commons:1.13.12
|    |    +--- org.springframework.boot:spring-boot-autoconfigure:3.3.10
|    |    |    \--- org.springframework.boot:spring-boot:3.3.10 (*)
|    |    +--- jakarta.annotation:jakarta.annotation-api:2.1.1
|    |    +--- org.springframework:spring-core:6.1.18 (*)
|    |    \--- org.yaml:snakeyaml:2.2
|    +--- org.springframework.boot:spring-boot-starter-json:3.3.10
|    |    +--- org.springframework.boot:spring-boot-starter:3.3.10 (*)
|    |    +--- org.springframework:spring-web:6.1.18
|    |    |    +--- org.springframework:spring-beans:6.1.18 (*)
|    |    |    +--- org.springframework:spring-core:6.1.18 (*)
|    |    |    \--- io.micrometer:micrometer-observation:1.12.12 -> 1.13.12 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-databind:2.17.3
|    |    |    +--- com.fasterxml.jackson.core:jackson-annotations:2.17.3
|    |    |    |    \--- com.fasterxml.jackson:jackson-bom:2.17.3
|    |    |    |         +--- com.fasterxml.jackson.core:jackson-annotations:2.17.3 (c)
|    |    |    |         +--- com.fasterxml.jackson.core:jackson-core:2.17.3 (c)
|    |    |    |         +--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (c)
|    |    |    |         +--- com.fasterxml.jackson.datatype:jackson-datatype-joda:2.17.3 (c)
|    |    |    |         +--- com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.17.3 (c)
|    |    |    |         +--- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.3 (c)
|    |    |    |         +--- com.fasterxml.jackson.module:jackson-module-parameter-names:2.17.3 (c)
|    |    |    |         +--- com.fasterxml.jackson.module:jackson-module-scala_2.12:2.17.3 (c)
|    |    |    |         \--- com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.3 (c)
|    |    |    +--- com.fasterxml.jackson.core:jackson-core:2.17.3
|    |    |    |    \--- com.fasterxml.jackson:jackson-bom:2.17.3 (*)
|    |    |    \--- com.fasterxml.jackson:jackson-bom:2.17.3 (*)
|    |    +--- com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.17.3
|    |    |    +--- com.fasterxml.jackson.core:jackson-core:2.17.3 (*)
|    |    |    +--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (*)
|    |    |    \--- com.fasterxml.jackson:jackson-bom:2.17.3 (*)
|    |    +--- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.3
|    |    |    +--- com.fasterxml.jackson.core:jackson-annotations:2.17.3 (*)
|    |    |    +--- com.fasterxml.jackson.core:jackson-core:2.17.3 (*)
|    |    |    +--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (*)
|    |    |    \--- com.fasterxml.jackson:jackson-bom:2.17.3 (*)
|    |    \--- com.fasterxml.jackson.module:jackson-module-parameter-names:2.17.3
|    |         +--- com.fasterxml.jackson.core:jackson-core:2.17.3 (*)
|    |         +--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (*)
|    |         \--- com.fasterxml.jackson:jackson-bom:2.17.3 (*)
|    +--- org.springframework.boot:spring-boot-starter-tomcat:3.3.10
|    |    +--- jakarta.annotation:jakarta.annotation-api:2.1.1
|    |    +--- org.apache.tomcat.embed:tomcat-embed-core:10.1.39
|    |    +--- org.apache.tomcat.embed:tomcat-embed-el:10.1.39
|    |    \--- org.apache.tomcat.embed:tomcat-embed-websocket:10.1.39
|    |         \--- org.apache.tomcat.embed:tomcat-embed-core:10.1.39
|    +--- org.springframework:spring-web:6.1.18 (*)
|    \--- org.springframework:spring-webmvc:6.1.18
|         +--- org.springframework:spring-aop:6.1.18 (*)
|         +--- org.springframework:spring-beans:6.1.18 (*)
|         +--- org.springframework:spring-context:6.1.18 (*)
|         +--- org.springframework:spring-core:6.1.18 (*)
|         +--- org.springframework:spring-expression:6.1.18 (*)
|         \--- org.springframework:spring-web:6.1.18 (*)
+--- org.springframework.boot:spring-boot-starter-batch -> 3.3.10
|    +--- org.springframework.boot:spring-boot-starter:3.3.10 (*)
|    +--- org.springframework.boot:spring-boot-starter-jdbc:3.3.10
|    |    +--- org.springframework.boot:spring-boot-starter:3.3.10 (*)
|    |    +--- com.zaxxer:HikariCP:5.1.0
|    |    |    \--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
|    |    \--- org.springframework:spring-jdbc:6.1.18
|    |         +--- org.springframework:spring-beans:6.1.18 (*)
|    |         +--- org.springframework:spring-core:6.1.18 (*)
|    |         \--- org.springframework:spring-tx:6.1.18
|    |              +--- org.springframework:spring-beans:6.1.18 (*)
|    |              \--- org.springframework:spring-core:6.1.18 (*)
|    \--- org.springframework.batch:spring-batch-core:5.1.3
|         +--- org.springframework.batch:spring-batch-infrastructure:5.1.3
|         |    +--- org.springframework:spring-core:6.1.16 -> 6.1.18 (*)
|         |    \--- org.springframework.retry:spring-retry:2.0.11
|         +--- org.springframework:spring-aop:6.1.16 -> 6.1.18 (*)
|         +--- org.springframework:spring-beans:6.1.16 -> 6.1.18 (*)
|         +--- org.springframework:spring-context:6.1.16 -> 6.1.18 (*)
|         +--- org.springframework:spring-tx:6.1.16 -> 6.1.18 (*)
|         +--- org.springframework:spring-jdbc:6.1.16 -> 6.1.18 (*)
|         +--- io.micrometer:micrometer-core:1.12.12 -> 1.13.12
|         |    +--- io.micrometer:micrometer-commons:1.13.12
|         |    +--- io.micrometer:micrometer-observation:1.13.12 (*)
|         |    +--- org.hdrhistogram:HdrHistogram:2.2.2
|         |    \--- org.latencyutils:LatencyUtils:2.0.3
|         \--- io.micrometer:micrometer-observation:1.12.12 -> 1.13.12 (*)
+--- org.springframework.boot:spring-boot-starter-webflux -> 3.3.10
|    +--- org.springframework.boot:spring-boot-starter:3.3.10 (*)
|    +--- org.springframework.boot:spring-boot-starter-json:3.3.10 (*)
|    +--- org.springframework.boot:spring-boot-starter-reactor-netty:3.3.10
|    |    \--- io.projectreactor.netty:reactor-netty-http:1.1.28
|    |         +--- io.netty:netty-codec-http:4.1.119.Final
|    |         |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    +--- io.netty:netty-buffer:4.1.119.Final
|    |         |    |    \--- io.netty:netty-common:4.1.119.Final
|    |         |    +--- io.netty:netty-transport:4.1.119.Final
|    |         |    |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    |    \--- io.netty:netty-resolver:4.1.119.Final
|    |         |    |         \--- io.netty:netty-common:4.1.119.Final
|    |         |    +--- io.netty:netty-codec:4.1.119.Final
|    |         |    |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    |    \--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |    \--- io.netty:netty-handler:4.1.119.Final
|    |         |         +--- io.netty:netty-common:4.1.119.Final
|    |         |         +--- io.netty:netty-resolver:4.1.119.Final (*)
|    |         |         +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |         +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |         +--- io.netty:netty-transport-native-unix-common:4.1.119.Final
|    |         |         |    +--- io.netty:netty-common:4.1.119.Final
|    |         |         |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |         |    \--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |         \--- io.netty:netty-codec:4.1.119.Final (*)
|    |         +--- io.netty:netty-codec-http2:4.1.119.Final
|    |         |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-codec:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-handler:4.1.119.Final (*)
|    |         |    \--- io.netty:netty-codec-http:4.1.119.Final (*)
|    |         +--- io.netty:netty-resolver-dns:4.1.119.Final
|    |         |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-resolver:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-codec:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-codec-dns:4.1.119.Final
|    |         |    |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |    |    \--- io.netty:netty-codec:4.1.119.Final (*)
|    |         |    \--- io.netty:netty-handler:4.1.119.Final (*)
|    |         +--- io.netty:netty-resolver-dns-native-macos:4.1.119.Final
|    |         |    \--- io.netty:netty-resolver-dns-classes-macos:4.1.119.Final
|    |         |         +--- io.netty:netty-common:4.1.119.Final
|    |         |         +--- io.netty:netty-resolver-dns:4.1.119.Final (*)
|    |         |         \--- io.netty:netty-transport-native-unix-common:4.1.119.Final (*)
|    |         +--- io.netty:netty-transport-native-epoll:4.1.119.Final
|    |         |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-transport-native-unix-common:4.1.119.Final (*)
|    |         |    \--- io.netty:netty-transport-classes-epoll:4.1.119.Final
|    |         |         +--- io.netty:netty-common:4.1.119.Final
|    |         |         +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |         +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |         \--- io.netty:netty-transport-native-unix-common:4.1.119.Final (*)
|    |         +--- io.projectreactor.netty:reactor-netty-core:1.1.28
|    |         |    +--- io.netty:netty-handler:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-handler-proxy:4.1.119.Final
|    |         |    |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |    |    +--- io.netty:netty-codec:4.1.119.Final (*)
|    |         |    |    +--- io.netty:netty-codec-socks:4.1.119.Final
|    |         |    |    |    +--- io.netty:netty-common:4.1.119.Final
|    |         |    |    |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |         |    |    |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |         |    |    |    \--- io.netty:netty-codec:4.1.119.Final (*)
|    |         |    |    \--- io.netty:netty-codec-http:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-resolver-dns:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-resolver-dns-native-macos:4.1.119.Final (*)
|    |         |    +--- io.netty:netty-transport-native-epoll:4.1.119.Final (*)
|    |         |    \--- io.projectreactor:reactor-core:3.5.20 -> 3.6.15
|    |         |         \--- org.reactivestreams:reactive-streams:1.0.4
|    |         \--- io.projectreactor:reactor-core:3.5.20 -> 3.6.15 (*)
|    +--- org.springframework:spring-web:6.1.18 (*)
|    \--- org.springframework:spring-webflux:6.1.18
|         +--- org.springframework:spring-beans:6.1.18 (*)
|         +--- org.springframework:spring-core:6.1.18 (*)
|         +--- org.springframework:spring-web:6.1.18 (*)
|         \--- io.projectreactor:reactor-core:3.6.15 (*)
+--- org.springframework.boot:spring-boot-starter-tomcat -> 3.3.10 (*)
+--- org.springframework.boot:spring-boot-starter-validation -> 3.3.10
|    +--- org.springframework.boot:spring-boot-starter:3.3.10 (*)
|    +--- org.apache.tomcat.embed:tomcat-embed-el:10.1.39
|    \--- org.hibernate.validator:hibernate-validator:8.0.2.Final
|         +--- jakarta.validation:jakarta.validation-api:3.0.2
|         +--- org.jboss.logging:jboss-logging:3.4.3.Final -> 3.5.3.Final
|         \--- com.fasterxml:classmate:1.5.1 -> 1.7.0
+--- org.springframework.boot:spring-boot-starter-data-jpa -> 3.3.10
|    +--- org.springframework.boot:spring-boot-starter-aop:3.3.10
|    |    +--- org.springframework.boot:spring-boot-starter:3.3.10 (*)
|    |    +--- org.springframework:spring-aop:6.1.18 (*)
|    |    \--- org.aspectj:aspectjweaver:1.9.23
|    +--- org.springframework.boot:spring-boot-starter-jdbc:3.3.10 (*)
|    +--- org.hibernate.orm:hibernate-core:6.5.3.Final
|    |    +--- jakarta.persistence:jakarta.persistence-api:3.1.0
|    |    +--- jakarta.transaction:jakarta.transaction-api:2.0.1
|    |    +--- org.jboss.logging:jboss-logging:3.5.0.Final -> 3.5.3.Final
|    |    +--- org.hibernate.common:hibernate-commons-annotations:6.0.6.Final
|    |    +--- io.smallrye:jandex:3.1.2
|    |    +--- com.fasterxml:classmate:1.5.1 -> 1.7.0
|    |    +--- net.bytebuddy:byte-buddy:1.14.15 -> 1.14.19
|    |    +--- jakarta.xml.bind:jakarta.xml.bind-api:4.0.0 -> 4.0.2
|    |    |    \--- jakarta.activation:jakarta.activation-api:2.1.3
|    |    +--- org.glassfish.jaxb:jaxb-runtime:4.0.2 -> 4.0.5
|    |    |    \--- org.glassfish.jaxb:jaxb-core:4.0.5
|    |    |         +--- jakarta.xml.bind:jakarta.xml.bind-api:4.0.2 (*)
|    |    |         +--- jakarta.activation:jakarta.activation-api:2.1.3
|    |    |         +--- org.eclipse.angus:angus-activation:2.0.2
|    |    |         |    \--- jakarta.activation:jakarta.activation-api:2.1.3
|    |    |         +--- org.glassfish.jaxb:txw2:4.0.5
|    |    |         \--- com.sun.istack:istack-commons-runtime:4.1.2
|    |    +--- jakarta.inject:jakarta.inject-api:2.0.1
|    |    \--- org.antlr:antlr4-runtime:4.13.0 -> 4.9.3
|    +--- org.springframework.data:spring-data-jpa:3.3.10
|    |    +--- org.springframework.data:spring-data-commons:3.3.10
|    |    |    +--- org.springframework:spring-core:6.1.18 (*)
|    |    |    +--- org.springframework:spring-beans:6.1.18 (*)
|    |    |    \--- org.slf4j:slf4j-api:2.0.2 -> 2.0.17
|    |    +--- org.springframework:spring-orm:6.1.18
|    |    |    +--- org.springframework:spring-beans:6.1.18 (*)
|    |    |    +--- org.springframework:spring-core:6.1.18 (*)
|    |    |    +--- org.springframework:spring-jdbc:6.1.18 (*)
|    |    |    \--- org.springframework:spring-tx:6.1.18 (*)
|    |    +--- org.springframework:spring-context:6.1.18 (*)
|    |    +--- org.springframework:spring-aop:6.1.18 (*)
|    |    +--- org.springframework:spring-tx:6.1.18 (*)
|    |    +--- org.springframework:spring-beans:6.1.18 (*)
|    |    +--- org.springframework:spring-core:6.1.18 (*)
|    |    +--- org.antlr:antlr4-runtime:4.13.0 -> 4.9.3
|    |    +--- jakarta.annotation:jakarta.annotation-api:2.0.0 -> 2.1.1
|    |    \--- org.slf4j:slf4j-api:2.0.2 -> 2.0.17
|    \--- org.springframework:spring-aspects:6.1.18
|         \--- org.aspectj:aspectjweaver:1.9.22.1 -> 1.9.23
+--- org.springframework.boot:spring-boot-starter-jdbc -> 3.3.10 (*)
+--- org.springframework.kafka:spring-kafka -> 3.2.8
|    +--- org.springframework:spring-context:6.1.18 (*)
|    +--- org.springframework:spring-messaging:6.1.18
|    |    +--- org.springframework:spring-beans:6.1.18 (*)
|    |    \--- org.springframework:spring-core:6.1.18 (*)
|    +--- org.springframework:spring-tx:6.1.18 (*)
|    +--- org.springframework.retry:spring-retry:2.0.11
|    +--- org.apache.kafka:kafka-clients:3.7.2
|    |    +--- com.github.luben:zstd-jni:1.5.6-4
|    |    +--- org.lz4:lz4-java:1.8.0
|    |    +--- org.xerial.snappy:snappy-java:1.1.10.5
|    |    \--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
|    \--- io.micrometer:micrometer-observation:1.13.12 (*)
+--- org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0
|    +--- org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0
|    |    +--- org.springdoc:springdoc-openapi-starter-common:2.6.0
|    |    |    +--- org.springframework.boot:spring-boot-autoconfigure:3.3.0 -> 3.3.10 (*)
|    |    |    \--- io.swagger.core.v3:swagger-core-jakarta:2.2.22
|    |    |         +--- org.apache.commons:commons-lang3:3.14.0
|    |    |         +--- org.slf4j:slf4j-api:2.0.9 -> 2.0.17
|    |    |         +--- io.swagger.core.v3:swagger-annotations-jakarta:2.2.22
|    |    |         +--- io.swagger.core.v3:swagger-models-jakarta:2.2.22
|    |    |         |    \--- com.fasterxml.jackson.core:jackson-annotations:2.16.2 -> 2.17.3 (*)
|    |    |         +--- org.yaml:snakeyaml:2.2
|    |    |         +--- jakarta.xml.bind:jakarta.xml.bind-api:3.0.1 -> 4.0.2 (*)
|    |    |         +--- jakarta.validation:jakarta.validation-api:3.0.2
|    |    |         +--- com.fasterxml.jackson.core:jackson-annotations:2.16.2 -> 2.17.3 (*)
|    |    |         +--- com.fasterxml.jackson.core:jackson-databind:2.16.2 -> 2.17.3 (*)
|    |    |         +--- com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.2 -> 2.17.3
|    |    |         |    +--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (*)
|    |    |         |    +--- org.yaml:snakeyaml:2.3 -> 2.2
|    |    |         |    +--- com.fasterxml.jackson.core:jackson-core:2.17.3 (*)
|    |    |         |    \--- com.fasterxml.jackson:jackson-bom:2.17.3 (*)
|    |    |         \--- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.2 -> 2.17.3 (*)
|    |    \--- org.springframework:spring-webmvc:6.1.8 -> 6.1.18 (*)
|    \--- org.webjars:swagger-ui:5.17.14
+--- jakarta.persistence:jakarta.persistence-api:3.1.0
+--- org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1
+--- javax.servlet:javax.servlet-api:4.0.1
+--- com.oracle.database.jdbc:ojdbc11:23.5.0.24.07
+--- com.opencsv:opencsv:5.11
|    +--- org.apache.commons:commons-lang3:3.17.0 -> 3.14.0
|    +--- org.apache.commons:commons-text:1.13.0
|    |    \--- org.apache.commons:commons-lang3:3.17.0 -> 3.14.0
|    +--- commons-beanutils:commons-beanutils:1.10.0
|    |    +--- commons-logging:commons-logging:1.3.4
|    |    \--- commons-collections:commons-collections:3.2.2
|    \--- org.apache.commons:commons-collections4:4.4
+--- org.apache.poi:poi-ooxml:5.4.1
|    +--- org.apache.poi:poi:5.4.1
|    |    +--- commons-codec:commons-codec:1.18.0 -> 1.16.1
|    |    +--- org.apache.commons:commons-collections4:4.4
|    |    +--- org.apache.commons:commons-math3:3.6.1
|    |    +--- commons-io:commons-io:2.18.0
|    |    +--- com.zaxxer:SparseBitSet:1.3
|    |    \--- org.apache.logging.log4j:log4j-api:2.24.3 -> 2.20.0
|    +--- org.apache.poi:poi-ooxml-lite:5.4.1
|    |    \--- org.apache.xmlbeans:xmlbeans:5.3.0
|    +--- org.apache.xmlbeans:xmlbeans:5.3.0
|    +--- org.apache.commons:commons-compress:1.27.1
|    |    +--- commons-codec:commons-codec:1.17.1 -> 1.16.1
|    |    +--- commons-io:commons-io:2.16.1 -> 2.18.0
|    |    \--- org.apache.commons:commons-lang3:3.16.0 -> 3.14.0
|    +--- commons-io:commons-io:2.18.0
|    +--- com.github.virtuald:curvesapi:1.08
|    +--- org.apache.logging.log4j:log4j-api:2.24.3 -> 2.20.0
|    \--- org.apache.commons:commons-collections4:4.4
+--- org.apache.spark:spark-core_2.12:3.5.5
|    +--- org.apache.avro:avro:1.11.4
|    |    +--- com.fasterxml.jackson.core:jackson-core:2.14.3 -> 2.17.3 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-databind:2.14.3 -> 2.17.3 (*)
|    |    \--- org.apache.commons:commons-compress:1.26.2 -> 1.27.1 (*)
|    +--- org.apache.avro:avro-mapred:1.11.4
|    |    +--- org.apache.avro:avro-ipc:1.11.4
|    |    |    +--- org.apache.avro:avro:1.11.4 (*)
|    |    |    +--- com.fasterxml.jackson.core:jackson-core:2.14.3 -> 2.17.3 (*)
|    |    |    +--- com.fasterxml.jackson.core:jackson-databind:2.14.3 -> 2.17.3 (*)
|    |    |    +--- org.xerial.snappy:snappy-java:1.1.10.5
|    |    |    \--- org.tukaani:xz:1.9
|    |    \--- com.fasterxml.jackson.core:jackson-core:2.14.3 -> 2.17.3 (*)
|    +--- com.twitter:chill_2.12:0.10.0
|    |    +--- org.scala-lang:scala-library:2.12.14 -> 2.12.19
|    |    +--- com.twitter:chill-java:0.10.0
|    |    |    \--- com.esotericsoftware:kryo-shaded:4.0.2
|    |    |         +--- com.esotericsoftware:minlog:1.3.0
|    |    |         \--- org.objenesis:objenesis:2.5.1
|    |    \--- com.esotericsoftware:kryo-shaded:4.0.2 (*)
|    +--- com.twitter:chill-java:0.10.0 (*)
|    +--- org.apache.xbean:xbean-asm9-shaded:4.23
|    +--- org.apache.hadoop:hadoop-client-api:3.3.4
|    |    \--- org.xerial.snappy:snappy-java:1.1.8.2 -> 1.1.10.5
|    +--- org.apache.hadoop:hadoop-client-runtime:3.3.4
|    |    +--- org.apache.hadoop:hadoop-client-api:3.3.4 (*)
|    |    +--- org.xerial.snappy:snappy-java:1.1.8.2 -> 1.1.10.5
|    |    +--- commons-logging:commons-logging:1.1.3 -> 1.3.4
|    |    \--- com.google.code.findbugs:jsr305:3.0.2
|    +--- org.apache.spark:spark-launcher_2.12:3.5.5
|    |    \--- org.apache.spark:spark-tags_2.12:3.5.5
|    |         \--- org.scala-lang:scala-library:2.12.18 -> 2.12.19
|    +--- org.apache.spark:spark-kvstore_2.12:3.5.5
|    |    +--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    |    +--- org.fusesource.leveldbjni:leveldbjni-all:1.8
|    |    +--- com.fasterxml.jackson.core:jackson-core:2.15.2 -> 2.17.3 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-databind:2.15.2 -> 2.17.3 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-annotations:2.15.2 -> 2.17.3 (*)
|    |    \--- org.rocksdb:rocksdbjni:8.3.2
|    +--- org.apache.spark:spark-network-common_2.12:3.5.5
|    |    +--- org.scala-lang:scala-library:2.12.18 -> 2.12.19
|    |    +--- io.netty:netty-all:4.1.96.Final -> 4.1.119.Final
|    |    |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-codec:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-codec-http:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-codec-http2:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-codec-socks:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-common:4.1.119.Final
|    |    |    +--- io.netty:netty-handler:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-transport-native-unix-common:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-handler-proxy:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-resolver:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-transport-classes-epoll:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-transport-classes-kqueue:4.1.119.Final
|    |    |    |    +--- io.netty:netty-common:4.1.119.Final
|    |    |    |    +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |    |    |    +--- io.netty:netty-transport:4.1.119.Final (*)
|    |    |    |    \--- io.netty:netty-transport-native-unix-common:4.1.119.Final (*)
|    |    |    +--- io.netty:netty-transport-native-epoll:4.1.119.Final (*)
|    |    |    \--- io.netty:netty-transport-native-kqueue:4.1.119.Final
|    |    |         +--- io.netty:netty-common:4.1.119.Final
|    |    |         +--- io.netty:netty-buffer:4.1.119.Final (*)
|    |    |         +--- io.netty:netty-transport:4.1.119.Final (*)
|    |    |         +--- io.netty:netty-transport-native-unix-common:4.1.119.Final (*)
|    |    |         \--- io.netty:netty-transport-classes-kqueue:4.1.119.Final (*)
|    |    +--- io.netty:netty-transport-native-epoll:4.1.96.Final -> 4.1.119.Final (*)
|    |    +--- io.netty:netty-transport-native-kqueue:4.1.96.Final -> 4.1.119.Final (*)
|    |    +--- org.apache.commons:commons-lang3:3.12.0 -> 3.14.0
|    |    +--- org.fusesource.leveldbjni:leveldbjni-all:1.8
|    |    +--- org.rocksdb:rocksdbjni:8.3.2
|    |    +--- com.fasterxml.jackson.core:jackson-databind:2.15.2 -> 2.17.3 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-annotations:2.15.2 -> 2.17.3 (*)
|    |    +--- io.dropwizard.metrics:metrics-core:4.2.19
|    |    +--- com.google.code.findbugs:jsr305:3.0.0 -> 3.0.2
|    |    +--- org.apache.commons:commons-crypto:1.1.0
|    |    +--- com.google.crypto.tink:tink:1.9.0
|    |    |    +--- com.google.code.findbugs:jsr305:3.0.2
|    |    |    +--- com.google.code.gson:gson:2.10.1
|    |    |    +--- com.google.protobuf:protobuf-java:3.19.6
|    |    |    \--- joda-time:joda-time:2.12.5
|    |    +--- org.roaringbitmap:RoaringBitmap:0.9.45
|    |    |    \--- org.roaringbitmap:shims:0.9.45
|    |    \--- org.apache.spark:spark-common-utils_2.12:3.5.5
|    |         +--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    |         +--- com.fasterxml.jackson.core:jackson-databind:2.15.2 -> 2.17.3 (*)
|    |         +--- com.fasterxml.jackson.module:jackson-module-scala_2.12:2.15.2 -> 2.17.3
|    |         |    +--- org.scala-lang:scala-library:2.12.19
|    |         |    +--- com.fasterxml.jackson.core:jackson-core:2.17.3 (*)
|    |         |    +--- com.fasterxml.jackson.core:jackson-annotations:2.17.3 (*)
|    |         |    +--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (*)
|    |         |    \--- com.thoughtworks.paranamer:paranamer:2.8
|    |         +--- org.apache.commons:commons-text:1.10.0 -> 1.13.0 (*)
|    |         +--- org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0 -> 2.23.1
|    |         \--- org.apache.logging.log4j:log4j-1.2-api:2.20.0 -> 2.23.1
|    +--- org.apache.spark:spark-network-shuffle_2.12:3.5.5
|    |    +--- org.apache.spark:spark-network-common_2.12:3.5.5 (*)
|    |    +--- io.dropwizard.metrics:metrics-core:4.2.19
|    |    +--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    |    \--- org.roaringbitmap:RoaringBitmap:0.9.45 (*)
|    +--- org.apache.spark:spark-unsafe_2.12:3.5.5
|    |    +--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    |    +--- org.apache.spark:spark-common-utils_2.12:3.5.5 (*)
|    |    +--- com.twitter:chill_2.12:0.10.0 (*)
|    |    \--- com.google.code.findbugs:jsr305:3.0.0 -> 3.0.2
|    +--- org.apache.spark:spark-common-utils_2.12:3.5.5 (*)
|    +--- javax.activation:activation:1.1.1
|    +--- org.apache.curator:curator-recipes:2.13.0
|    |    \--- org.apache.curator:curator-framework:2.13.0
|    |         \--- org.apache.curator:curator-client:2.13.0
|    |              +--- org.apache.zookeeper:zookeeper:3.4.8 -> 3.6.3
|    |              |    +--- org.apache.zookeeper:zookeeper-jute:3.6.3
|    |              |    |    \--- org.apache.yetus:audience-annotations:0.5.0 -> 0.13.0
|    |              |    +--- org.apache.yetus:audience-annotations:0.5.0 -> 0.13.0
|    |              |    +--- io.netty:netty-handler:4.1.63.Final -> 4.1.119.Final (*)
|    |              |    \--- io.netty:netty-transport-native-epoll:4.1.63.Final -> 4.1.119.Final (*)
|    |              \--- com.google.guava:guava:16.0.1 -> 17.0
|    +--- org.apache.zookeeper:zookeeper:3.6.3 (*)
|    +--- jakarta.servlet:jakarta.servlet-api:4.0.3 -> 6.0.0
|    +--- commons-codec:commons-codec:1.16.1
|    +--- org.apache.commons:commons-compress:1.23.0 -> 1.27.1 (*)
|    +--- org.apache.commons:commons-lang3:3.12.0 -> 3.14.0
|    +--- org.apache.commons:commons-math3:3.6.1
|    +--- org.apache.commons:commons-text:1.10.0 -> 1.13.0 (*)
|    +--- commons-io:commons-io:2.16.1 -> 2.18.0
|    +--- commons-collections:commons-collections:3.2.2
|    +--- org.apache.commons:commons-collections4:4.4
|    +--- com.google.code.findbugs:jsr305:3.0.0 -> 3.0.2
|    +--- com.ning:compress-lzf:1.1.2
|    +--- org.xerial.snappy:snappy-java:1.1.10.5
|    +--- org.lz4:lz4-java:1.8.0
|    +--- com.github.luben:zstd-jni:1.5.5-4 -> 1.5.6-4
|    +--- org.roaringbitmap:RoaringBitmap:0.9.45 (*)
|    +--- org.scala-lang.modules:scala-xml_2.12:2.1.0
|    |    \--- org.scala-lang:scala-library:2.12.15 -> 2.12.19
|    +--- org.scala-lang.modules:scala-collection-compat_2.12:2.7.0 -> 2.11.0
|    |    \--- org.scala-lang:scala-library:2.12.17 -> 2.12.19
|    +--- org.scala-lang:scala-library:2.12.18 -> 2.12.19
|    +--- org.scala-lang:scala-reflect:2.12.18
|    |    \--- org.scala-lang:scala-library:2.12.18 -> 2.12.19
|    +--- org.json4s:json4s-jackson_2.12:3.7.0-M11
|    |    +--- org.scala-lang:scala-library:2.12.13 -> 2.12.19
|    |    \--- org.json4s:json4s-core_2.12:3.7.0-M11
|    |         +--- org.scala-lang:scala-library:2.12.13 -> 2.12.19
|    |         +--- org.json4s:json4s-ast_2.12:3.7.0-M11
|    |         |    \--- org.scala-lang:scala-library:2.12.13 -> 2.12.19
|    |         +--- org.json4s:json4s-scalap_2.12:3.7.0-M11
|    |         |    \--- org.scala-lang:scala-library:2.12.13 -> 2.12.19
|    |         \--- com.thoughtworks.paranamer:paranamer:2.8
|    +--- org.glassfish.jersey.core:jersey-client:2.40 -> 3.1.10
|    |    +--- jakarta.ws.rs:jakarta.ws.rs-api:3.1.0
|    |    +--- org.glassfish.jersey.core:jersey-common:3.1.10
|    |    |    +--- jakarta.ws.rs:jakarta.ws.rs-api:3.1.0
|    |    |    +--- jakarta.annotation:jakarta.annotation-api:2.1.1
|    |    |    +--- jakarta.inject:jakarta.inject-api:2.0.1
|    |    |    \--- org.glassfish.hk2:osgi-resource-locator:1.0.3
|    |    \--- jakarta.inject:jakarta.inject-api:2.0.1
|    +--- org.glassfish.jersey.core:jersey-common:2.40 -> 3.1.10 (*)
|    +--- org.glassfish.jersey.core:jersey-server:2.40 -> 3.1.10
|    |    +--- org.glassfish.jersey.core:jersey-common:3.1.10 (*)
|    |    +--- org.glassfish.jersey.core:jersey-client:3.1.10 (*)
|    |    +--- jakarta.ws.rs:jakarta.ws.rs-api:3.1.0
|    |    +--- jakarta.annotation:jakarta.annotation-api:2.1.1
|    |    +--- jakarta.inject:jakarta.inject-api:2.0.1
|    |    \--- jakarta.validation:jakarta.validation-api:3.0.2
|    +--- org.glassfish.jersey.containers:jersey-container-servlet:2.40 -> 3.1.10
|    |    +--- org.glassfish.jersey.containers:jersey-container-servlet-core:3.1.10
|    |    |    +--- jakarta.inject:jakarta.inject-api:2.0.1
|    |    |    +--- org.glassfish.jersey.core:jersey-common:3.1.10 (*)
|    |    |    +--- org.glassfish.jersey.core:jersey-server:3.1.10 (*)
|    |    |    \--- jakarta.ws.rs:jakarta.ws.rs-api:3.1.0
|    |    +--- org.glassfish.jersey.core:jersey-common:3.1.10 (*)
|    |    +--- org.glassfish.jersey.core:jersey-server:3.1.10 (*)
|    |    \--- jakarta.ws.rs:jakarta.ws.rs-api:3.1.0
|    +--- org.glassfish.jersey.containers:jersey-container-servlet-core:2.40 -> 3.1.10 (*)
|    +--- org.glassfish.jersey.inject:jersey-hk2:2.40 -> 3.1.10
|    |    +--- org.glassfish.jersey.core:jersey-common:3.1.10 (*)
|    |    +--- org.glassfish.hk2:hk2-locator:3.0.6
|    |    |    +--- org.glassfish.hk2.external:aopalliance-repackaged:3.0.6
|    |    |    +--- org.glassfish.hk2:hk2-api:3.0.6
|    |    |    |    +--- org.glassfish.hk2:hk2-utils:3.0.6
|    |    |    |    \--- org.glassfish.hk2.external:aopalliance-repackaged:3.0.6
|    |    |    \--- org.glassfish.hk2:hk2-utils:3.0.6
|    |    \--- org.javassist:javassist:3.30.2-GA
|    +--- io.netty:netty-all:4.1.96.Final -> 4.1.119.Final (*)
|    +--- io.netty:netty-transport-native-epoll:4.1.96.Final -> 4.1.119.Final (*)
|    +--- io.netty:netty-transport-native-kqueue:4.1.96.Final -> 4.1.119.Final (*)
|    +--- com.clearspring.analytics:stream:2.9.6
|    +--- io.dropwizard.metrics:metrics-core:4.2.19
|    +--- io.dropwizard.metrics:metrics-jvm:4.2.19
|    |    \--- io.dropwizard.metrics:metrics-core:4.2.19
|    +--- io.dropwizard.metrics:metrics-json:4.2.19
|    |    +--- io.dropwizard.metrics:metrics-core:4.2.19
|    |    +--- com.fasterxml.jackson.core:jackson-core:2.12.7 -> 2.17.3 (*)
|    |    \--- com.fasterxml.jackson.core:jackson-databind:2.12.7.1 -> 2.17.3 (*)
|    +--- io.dropwizard.metrics:metrics-graphite:4.2.19
|    |    \--- io.dropwizard.metrics:metrics-core:4.2.19
|    +--- io.dropwizard.metrics:metrics-jmx:4.2.19
|    |    \--- io.dropwizard.metrics:metrics-core:4.2.19
|    +--- com.fasterxml.jackson.core:jackson-databind:2.15.2 -> 2.17.3 (*)
|    +--- com.fasterxml.jackson.module:jackson-module-scala_2.12:2.15.2 -> 2.17.3 (*)
|    +--- org.apache.ivy:ivy:2.5.1
|    +--- oro:oro:2.0.8
|    +--- net.razorvine:pickle:1.3
|    +--- net.sf.py4j:py4j:0.10.9.7
|    +--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    \--- org.apache.commons:commons-crypto:1.1.0
+--- org.apache.spark:spark-sql_2.12:3.5.5
|    +--- org.rocksdb:rocksdbjni:8.3.2
|    +--- com.univocity:univocity-parsers:2.9.1
|    +--- org.apache.spark:spark-sketch_2.12:3.5.5
|    |    \--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    +--- org.apache.spark:spark-core_2.12:3.5.5 (*)
|    +--- org.apache.spark:spark-catalyst_2.12:3.5.5
|    |    +--- org.apache.spark:spark-core_2.12:3.5.5 (*)
|    |    +--- org.apache.spark:spark-sql-api_2.12:3.5.5
|    |    |    +--- org.scala-lang:scala-reflect:2.12.18 (*)
|    |    |    +--- org.scala-lang.modules:scala-parser-combinators_2.12:2.3.0
|    |    |    |    \--- org.scala-lang:scala-library:2.12.17 -> 2.12.19
|    |    |    +--- org.apache.spark:spark-common-utils_2.12:3.5.5 (*)
|    |    |    +--- org.apache.spark:spark-unsafe_2.12:3.5.5 (*)
|    |    |    +--- org.json4s:json4s-jackson_2.12:3.7.0-M11 (*)
|    |    |    +--- org.antlr:antlr4-runtime:4.9.3
|    |    |    +--- org.apache.arrow:arrow-vector:12.0.1
|    |    |    |    +--- org.apache.arrow:arrow-format:12.0.1
|    |    |    |    |    \--- com.google.flatbuffers:flatbuffers-java:1.12.0
|    |    |    |    +--- org.apache.arrow:arrow-memory-core:12.0.1
|    |    |    |    |    \--- com.google.code.findbugs:jsr305:3.0.2
|    |    |    |    +--- com.fasterxml.jackson.core:jackson-databind:2.15.1 -> 2.17.3 (*)
|    |    |    |    +--- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.1 -> 2.17.3 (*)
|    |    |    |    +--- commons-codec:commons-codec:1.15 -> 1.16.1
|    |    |    |    \--- com.google.flatbuffers:flatbuffers-java:1.12.0
|    |    |    \--- org.apache.arrow:arrow-memory-netty:12.0.1
|    |    |         \--- org.apache.arrow:arrow-memory-core:12.0.1 (*)
|    |    +--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    |    +--- org.apache.spark:spark-unsafe_2.12:3.5.5 (*)
|    |    +--- org.apache.spark:spark-sketch_2.12:3.5.5 (*)
|    |    +--- org.codehaus.janino:janino:3.1.9 -> 3.1.12
|    |    |    \--- org.codehaus.janino:commons-compiler:3.1.12
|    |    +--- org.codehaus.janino:commons-compiler:3.1.9 -> 3.1.12
|    |    +--- commons-codec:commons-codec:1.16.1
|    |    +--- com.univocity:univocity-parsers:2.9.1
|    |    \--- org.apache.datasketches:datasketches-java:3.3.0
|    |         \--- org.apache.datasketches:datasketches-memory:2.1.0
|    +--- org.apache.spark:spark-tags_2.12:3.5.5 (*)
|    +--- org.apache.orc:orc-core:1.9.5
|    |    +--- org.apache.orc:orc-shims:1.9.5
|    |    +--- org.apache.commons:commons-lang3:3.12.0 -> 3.14.0
|    |    +--- io.airlift:aircompressor:0.27
|    |    +--- org.jetbrains:annotations:17.0.0
|    |    \--- org.threeten:threeten-extra:1.7.1
|    +--- org.apache.orc:orc-mapreduce:1.9.5
|    |    \--- org.apache.commons:commons-lang3:3.12.0 -> 3.14.0
|    +--- org.apache.hive:hive-storage-api:2.8.1
|    +--- org.apache.parquet:parquet-column:1.13.1
|    |    +--- org.apache.parquet:parquet-common:1.13.1
|    |    |    \--- org.apache.parquet:parquet-format-structures:1.13.1
|    |    +--- org.apache.parquet:parquet-encoding:1.13.1
|    |    |    \--- org.apache.parquet:parquet-common:1.13.1 (*)
|    |    \--- org.apache.yetus:audience-annotations:0.13.0
|    +--- org.apache.parquet:parquet-hadoop:1.13.1
|    |    +--- org.apache.parquet:parquet-column:1.13.1 (*)
|    |    +--- org.apache.parquet:parquet-format-structures:1.13.1
|    |    +--- org.apache.parquet:parquet-common:1.13.1 (*)
|    |    +--- org.apache.parquet:parquet-jackson:1.13.1
|    |    +--- org.xerial.snappy:snappy-java:1.1.8.3 -> 1.1.10.5
|    |    +--- io.airlift:aircompressor:0.21 -> 0.27
|    |    +--- com.github.luben:zstd-jni:1.5.0-1 -> 1.5.6-4
|    |    \--- org.apache.yetus:audience-annotations:0.13.0
|    +--- com.fasterxml.jackson.core:jackson-databind:2.15.2 -> 2.17.3 (*)
|    \--- org.apache.xbean:xbean-asm9-shaded:4.23
+--- org.apache.logging.log4j:log4j-api:2.20.0
+--- org.apache.logging.log4j:log4j-core:2.20.0
|    \--- org.apache.logging.log4j:log4j-api:2.20.0
+--- com.crealytics:spark-excel_2.12:3.5.0_0.20.3
|    +--- org.apache.poi:poi:5.2.5 -> 5.4.1 (*)
|    +--- org.apache.poi:poi-ooxml:5.2.5 -> 5.4.1 (*)
|    +--- org.apache.poi:poi-ooxml-lite:5.2.5 -> 5.4.1 (*)
|    +--- org.apache.xmlbeans:xmlbeans:5.2.0 -> 5.3.0
|    +--- com.norbitltd:spoiwo_2.12:2.2.1
|    |    +--- org.scala-lang:scala-library:2.12.15 -> 2.12.19
|    |    +--- com.github.tototoshi:scala-csv_2.12:1.3.10
|    |    |    \--- org.scala-lang:scala-library:2.12.15 -> 2.12.19
|    |    +--- org.apache.poi:poi:5.2.1 -> 5.4.1 (*)
|    |    \--- org.apache.poi:poi-ooxml:5.2.1 -> 5.4.1 (*)
|    +--- com.github.pjfanning:excel-streaming-reader:4.2.1
|    |    +--- com.github.pjfanning:poi-shared-strings:2.7.1
|    |    |    +--- com.h2database:h2:2.2.224
|    |    |    +--- org.apache.poi:poi-ooxml:5.2.5 -> 5.4.1 (*)
|    |    |    +--- org.apache.poi:poi:5.2.5 -> 5.4.1 (*)
|    |    |    +--- org.apache.xmlbeans:xmlbeans:5.2.0 -> 5.3.0
|    |    |    \--- org.apache.commons:commons-text:1.11.0 -> 1.13.0 (*)
|    |    +--- commons-io:commons-io:2.15.0 -> 2.18.0
|    |    +--- org.apache.poi:poi:5.2.5 -> 5.4.1 (*)
|    |    \--- org.apache.poi:poi-ooxml:5.2.5 -> 5.4.1 (*)
|    +--- com.github.pjfanning:poi-shared-strings:2.7.1 (*)
|    +--- commons-io:commons-io:2.15.1 -> 2.18.0
|    +--- org.apache.commons:commons-compress:1.25.0 -> 1.27.1 (*)
|    +--- com.zaxxer:SparseBitSet:1.3
|    +--- org.apache.commons:commons-collections4:4.4
|    +--- com.github.virtuald:curvesapi:1.08
|    +--- commons-codec:commons-codec:1.16.0 -> 1.16.1
|    +--- org.apache.commons:commons-math3:3.6.1
|    +--- org.scala-lang.modules:scala-collection-compat_2.12:2.11.0 (*)
|    \--- org.scala-lang:scala-library:2.12.18 -> 2.12.19
+--- software.amazon.awssdk:s3:2.30.6
|    +--- software.amazon.awssdk:aws-xml-protocol:2.30.6
|    |    +--- software.amazon.awssdk:aws-query-protocol:2.30.6
|    |    |    +--- software.amazon.awssdk:protocol-core:2.30.6
|    |    |    |    +--- software.amazon.awssdk:sdk-core:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:http-client-spi:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6
|    |    |    |    |    |    |    +--- org.reactivestreams:reactive-streams:1.0.4
|    |    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    |    \--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
|    |    |    |    |    |    +--- software.amazon.awssdk:metrics-spi:2.30.6
|    |    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    |    \--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    |    \--- org.reactivestreams:reactive-streams:1.0.4
|    |    |    |    |    +--- software.amazon.awssdk:metrics-spi:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:endpoints-spi:2.30.6
|    |    |    |    |    |    \--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:http-auth-spi:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    |    |    |    |    +--- org.reactivestreams:reactive-streams:1.0.4
|    |    |    |    |    |    \--- software.amazon.awssdk:identity-spi:2.30.6
|    |    |    |    |    |         +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |         \--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:http-auth-aws:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:identity-spi:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:http-auth-spi:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:checksums-spi:2.30.6
|    |    |    |    |    |    |    \--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    \--- software.amazon.awssdk:checksums:2.30.6
|    |    |    |    |    |         +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |         +--- software.amazon.awssdk:checksums-spi:2.30.6 (*)
|    |    |    |    |    |         \--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:checksums-spi:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:checksums:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:identity-spi:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:profiles:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    |    \--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:retries-spi:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    \--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:retries:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:retries-spi:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    \--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    +--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
|    |    |    |    |    \--- org.reactivestreams:reactive-streams:1.0.4
|    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    \--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    |    +--- software.amazon.awssdk:aws-core:2.30.6
|    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    +--- software.amazon.awssdk:regions:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:sdk-core:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:profiles:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:json-utils:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    \--- software.amazon.awssdk:third-party-jackson-core:2.30.6
|    |    |    |    |    \--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
|    |    |    |    +--- software.amazon.awssdk:auth:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:sdk-core:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:identity-spi:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:regions:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:profiles:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:json-utils:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:http-auth-aws:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:http-auth-aws-eventstream:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    \--- software.amazon.eventstream:eventstream:1.0.1
|    |    |    |    |    +--- software.amazon.awssdk:http-auth:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    |    |    |    |    +--- software.amazon.awssdk:http-auth-spi:2.30.6 (*)
|    |    |    |    |    |    \--- software.amazon.awssdk:identity-spi:2.30.6 (*)
|    |    |    |    |    +--- software.amazon.awssdk:http-auth-spi:2.30.6 (*)
|    |    |    |    |    \--- software.amazon.eventstream:eventstream:1.0.1
|    |    |    |    +--- software.amazon.awssdk:http-auth-spi:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:identity-spi:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:http-auth:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:profiles:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:sdk-core:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:metrics-spi:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:endpoints-spi:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:retries-spi:2.30.6 (*)
|    |    |    |    +--- software.amazon.awssdk:retries:2.30.6 (*)
|    |    |    |    \--- software.amazon.eventstream:eventstream:1.0.1
|    |    |    +--- software.amazon.awssdk:sdk-core:2.30.6 (*)
|    |    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    |    \--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    +--- software.amazon.awssdk:protocol-core:2.30.6 (*)
|    |    +--- software.amazon.awssdk:aws-core:2.30.6 (*)
|    |    +--- software.amazon.awssdk:sdk-core:2.30.6 (*)
|    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    \--- software.amazon.awssdk:utils:2.30.6 (*)
|    +--- software.amazon.awssdk:protocol-core:2.30.6 (*)
|    +--- software.amazon.awssdk:arns:2.30.6
|    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    \--- software.amazon.awssdk:utils:2.30.6 (*)
|    +--- software.amazon.awssdk:profiles:2.30.6 (*)
|    +--- software.amazon.awssdk:crt-core:2.30.6
|    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    \--- software.amazon.awssdk:utils:2.30.6 (*)
|    +--- software.amazon.awssdk:http-auth:2.30.6 (*)
|    +--- software.amazon.awssdk:identity-spi:2.30.6 (*)
|    +--- software.amazon.awssdk:http-auth-spi:2.30.6 (*)
|    +--- software.amazon.awssdk:http-auth-aws:2.30.6 (*)
|    +--- software.amazon.awssdk:checksums:2.30.6 (*)
|    +--- software.amazon.awssdk:checksums-spi:2.30.6 (*)
|    +--- software.amazon.awssdk:retries-spi:2.30.6 (*)
|    +--- software.amazon.awssdk:sdk-core:2.30.6 (*)
|    +--- software.amazon.awssdk:auth:2.30.6 (*)
|    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    +--- software.amazon.awssdk:regions:2.30.6 (*)
|    +--- software.amazon.awssdk:annotations:2.30.6
|    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    +--- software.amazon.awssdk:aws-core:2.30.6 (*)
|    +--- software.amazon.awssdk:metrics-spi:2.30.6 (*)
|    +--- software.amazon.awssdk:json-utils:2.30.6 (*)
|    +--- software.amazon.awssdk:endpoints-spi:2.30.6 (*)
|    +--- software.amazon.awssdk:apache-client:2.30.6
|    |    +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|    |    +--- software.amazon.awssdk:metrics-spi:2.30.6 (*)
|    |    +--- software.amazon.awssdk:utils:2.30.6 (*)
|    |    +--- software.amazon.awssdk:annotations:2.30.6
|    |    +--- org.apache.httpcomponents:httpclient:4.5.13 -> 4.5.14
|    |    |    +--- org.apache.httpcomponents:httpcore:4.4.16
|    |    |    +--- commons-logging:commons-logging:1.2 -> 1.3.4
|    |    |    \--- commons-codec:commons-codec:1.11 -> 1.16.1
|    |    +--- org.apache.httpcomponents:httpcore:4.4.16
|    |    \--- commons-codec:commons-codec:1.17.1 -> 1.16.1
|    \--- software.amazon.awssdk:netty-nio-client:2.30.6
|         +--- software.amazon.awssdk:annotations:2.30.6
|         +--- software.amazon.awssdk:http-client-spi:2.30.6 (*)
|         +--- software.amazon.awssdk:utils:2.30.6 (*)
|         +--- software.amazon.awssdk:metrics-spi:2.30.6 (*)
|         +--- io.netty:netty-codec-http:4.1.115.Final -> 4.1.119.Final (*)
|         +--- io.netty:netty-codec-http2:4.1.115.Final -> 4.1.119.Final (*)
|         +--- io.netty:netty-codec:4.1.115.Final -> 4.1.119.Final (*)
|         +--- io.netty:netty-transport:4.1.115.Final -> 4.1.119.Final (*)
|         +--- io.netty:netty-common:4.1.115.Final -> 4.1.119.Final
|         +--- io.netty:netty-buffer:4.1.115.Final -> 4.1.119.Final (*)
|         +--- io.netty:netty-handler:4.1.115.Final -> 4.1.119.Final (*)
|         +--- io.netty:netty-transport-classes-epoll:4.1.115.Final -> 4.1.119.Final (*)
|         +--- io.netty:netty-resolver:4.1.115.Final -> 4.1.119.Final (*)
|         +--- org.reactivestreams:reactive-streams:1.0.4
|         \--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
+--- org.hibernate.orm:hibernate-envers -> 6.5.3.Final
|    +--- org.hibernate.orm:hibernate-core:6.5.3.Final (*)
|    +--- org.jboss.logging:jboss-logging:3.5.0.Final -> 3.5.3.Final
|    +--- jakarta.xml.bind:jakarta.xml.bind-api:4.0.0 -> 4.0.2 (*)
|    +--- org.glassfish.jaxb:jaxb-runtime:4.0.2 -> 4.0.5 (*)
|    +--- org.hibernate.common:hibernate-commons-annotations:6.0.6.Final
|    \--- io.smallrye:jandex:3.1.2
+--- com.sbi.epay:logging-service:0.1.0
|    +--- ch.qos.logback:logback-classic:1.5.18 -> 1.5.14
|    |    +--- ch.qos.logback:logback-core:1.5.14 -> 1.5.18
|    |    \--- org.slf4j:slf4j-api:2.0.15 -> 2.0.17
|    \--- net.logstash.logback:logstash-logback-encoder:7.4
|         \--- com.fasterxml.jackson.core:jackson-databind:2.15.2 -> 2.17.3 (*)
+--- name:cache-management-service-0.0.1
+--- ch.qos.logback:logback-classic:1.5.14 (*)
+--- net.logstash.logback:logstash-logback-encoder:7.4 (*)
+--- org.slf4j:jul-to-slf4j:2.0.9
|    \--- org.slf4j:slf4j-api:2.0.9 -> 2.0.17
+--- org.slf4j:jcl-over-slf4j:2.0.9
|    \--- org.slf4j:slf4j-api:2.0.9 -> 2.0.17
+--- com.vmware.gemfire:gemfire-core:10.1.2
|    +--- antlr:antlr:2.7.7
|    +--- commons-io:commons-io:2.16.1 -> 2.18.0
|    +--- io.micrometer:micrometer-core:1.12.1 -> 1.13.12 (*)
|    +--- javax.resource:javax.resource-api:1.7.1
|    |    \--- javax.transaction:javax.transaction-api:1.3
|    +--- org.apache.shiro:shiro-core:1.13.0
|    |    +--- org.apache.shiro:shiro-lang:1.13.0
|    |    |    \--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
|    |    +--- org.apache.shiro:shiro-cache:1.13.0
|    |    |    \--- org.apache.shiro:shiro-lang:1.13.0 (*)
|    |    +--- org.apache.shiro:shiro-crypto-hash:1.13.0
|    |    |    +--- org.apache.shiro:shiro-lang:1.13.0 (*)
|    |    |    \--- org.apache.shiro:shiro-crypto-core:1.13.0
|    |    |         \--- org.apache.shiro:shiro-lang:1.13.0 (*)
|    |    +--- org.apache.shiro:shiro-crypto-cipher:1.13.0
|    |    |    +--- org.apache.shiro:shiro-lang:1.13.0 (*)
|    |    |    \--- org.apache.shiro:shiro-crypto-core:1.13.0 (*)
|    |    +--- org.apache.shiro:shiro-config-core:1.13.0
|    |    |    \--- org.apache.shiro:shiro-lang:1.13.0 (*)
|    |    +--- org.apache.shiro:shiro-config-ogdl:1.13.0
|    |    |    +--- org.apache.shiro:shiro-lang:1.13.0 (*)
|    |    |    +--- org.apache.shiro:shiro-config-core:1.13.0 (*)
|    |    |    +--- org.apache.shiro:shiro-event:1.13.0
|    |    |    |    \--- org.apache.shiro:shiro-lang:1.13.0 (*)
|    |    |    +--- commons-beanutils:commons-beanutils:1.9.4 -> 1.10.0 (*)
|    |    |    \--- org.slf4j:slf4j-api:1.7.36 -> 2.0.17
|    |    \--- org.apache.shiro:shiro-event:1.13.0 (*)
|    +--- com.vmware.gemfire:gemfire-common:10.1.2
|    |    +--- com.fasterxml.jackson.core:jackson-databind:2.16.1 -> 2.17.3 (*)
|    |    +--- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1 -> 2.17.3 (*)
|    |    \--- com.fasterxml.jackson.datatype:jackson-datatype-joda:2.16.1 -> 2.17.3
|    |         +--- com.fasterxml.jackson.core:jackson-annotations:2.17.3 (*)
|    |         +--- com.fasterxml.jackson.core:jackson-core:2.17.3 (*)
|    |         +--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (*)
|    |         +--- joda-time:joda-time:2.10.14 -> 2.12.5
|    |         \--- com.fasterxml.jackson:jackson-bom:2.17.3 (*)
|    +--- com.vmware.gemfire:gemfire-serialization:10.1.2
|    |    +--- com.vmware.gemfire:gemfire-common:10.1.2 (*)
|    |    +--- com.vmware.gemfire:gemfire-logging:10.1.2
|    |    |    +--- com.vmware.gemfire:gemfire-common:10.1.2 (*)
|    |    |    \--- org.apache.logging.log4j:log4j-api:2.22.1 -> 2.20.0
|    |    +--- it.unimi.dsi:fastutil:8.5.12
|    |    \--- org.apache.commons:commons-lang3:3.13.0 -> 3.14.0
|    +--- com.vmware.gemfire:gemfire-management:10.1.2
|    |    +--- com.vmware.gemfire:gemfire-serialization:10.1.2 (*)
|    |    +--- org.apache.commons:commons-lang3:3.13.0 -> 3.14.0
|    |    +--- commons-io:commons-io:2.16.1 -> 2.18.0
|    |    +--- com.fasterxml.jackson.core:jackson-databind:2.16.1 -> 2.17.3 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-core:2.16.1 -> 2.17.3 (*)
|    |    +--- com.fasterxml.jackson.core:jackson-annotations:2.16.1 -> 2.17.3 (*)
|    |    +--- org.springframework:spring-web:5.3.39 -> 6.1.18 (*)
|    |    \--- org.apache.httpcomponents:httpclient:4.5.14 (*)
|    +--- org.jgroups:jgroups:3.6.14.Final
|    +--- com.fasterxml.jackson.core:jackson-annotations:2.16.1 -> 2.17.3 (*)
|    +--- com.fasterxml.jackson.core:jackson-databind:2.16.1 -> 2.17.3 (*)
|    +--- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1 -> 2.17.3 (*)
|    +--- com.fasterxml.jackson.datatype:jackson-datatype-joda:2.16.1 -> 2.17.3 (*)
|    +--- org.mongodb:bson:4.11.1 -> 5.0.1
|    +--- commons-validator:commons-validator:1.8.0
|    |    +--- commons-beanutils:commons-beanutils:1.9.4 -> 1.10.0 (*)
|    |    +--- commons-digester:commons-digester:2.1
|    |    +--- commons-logging:commons-logging:1.3.0 -> 1.3.4
|    |    \--- commons-collections:commons-collections:3.2.2
|    +--- javax.xml.bind:jaxb-api:2.3.1
|    |    \--- javax.activation:javax.activation-api:1.2.0
|    +--- com.sun.xml.bind:jaxb-impl:2.3.2 -> 4.0.5
|    |    \--- com.sun.xml.bind:jaxb-core:4.0.5
|    |         +--- jakarta.xml.bind:jakarta.xml.bind-api:4.0.2 (*)
|    |         \--- org.eclipse.angus:angus-activation:2.0.2 (*)
|    +--- org.apache.commons:commons-lang3:3.13.0 -> 3.14.0
|    +--- it.unimi.dsi:fastutil:8.5.12
|    +--- net.java.dev.jna:jna:5.13.0
|    +--- net.java.dev.jna:jna-platform:5.13.0
|    |    \--- net.java.dev.jna:jna:5.13.0
|    +--- net.sf.jopt-simple:jopt-simple:5.0.4
|    +--- org.apache.logging.log4j:log4j-api:2.22.1 -> 2.20.0
|    +--- io.github.classgraph:classgraph:4.8.165
|    +--- com.healthmarketscience.rmiio:rmiio:2.1.2
|    |    \--- commons-logging:commons-logging:1.1.3 -> 1.3.4
|    +--- com.vmware.gemfire:gemfire-logging:10.1.2 (*)
|    +--- com.vmware.gemfire:gemfire-membership:10.1.2
|    |    +--- com.vmware.gemfire:gemfire-common:10.1.2 (*)
|    |    +--- com.vmware.gemfire:gemfire-logging:10.1.2 (*)
|    |    +--- com.vmware.gemfire:gemfire-serialization:10.1.2 (*)
|    |    +--- com.vmware.gemfire:gemfire-tcp-messenger:10.1.2
|    |    |    +--- io.netty:netty-handler:4.1.115.Final -> 4.1.119.Final (*)
|    |    |    +--- commons-validator:commons-validator:1.8.0 (*)
|    |    |    +--- com.vmware.gemfire:gemfire-logging:10.1.2 (*)
|    |    |    \--- com.vmware.gemfire:gemfire-serialization:10.1.2 (*)
|    |    +--- com.vmware.gemfire:gemfire-tcp-server:10.1.2
|    |    |    +--- com.vmware.gemfire:gemfire-logging:10.1.2 (*)
|    |    |    +--- com.vmware.gemfire:gemfire-serialization:10.1.2 (*)
|    |    |    +--- com.vmware.gemfire:gemfire-common:10.1.2 (*)
|    |    |    \--- commons-validator:commons-validator:1.8.0 (*)
|    |    +--- org.jgroups:jgroups:3.6.14.Final
|    |    +--- org.apache.commons:commons-lang3:3.13.0 -> 3.14.0
|    |    +--- it.unimi.dsi:fastutil:8.5.12
|    |    \--- commons-validator:commons-validator:1.8.0 (*)
|    +--- com.vmware.gemfire:gemfire-tcp-messenger:10.1.2 (*)
|    +--- com.vmware.gemfire:gemfire-unsafe:10.1.2
|    +--- com.vmware.gemfire:gemfire-tcp-server:10.1.2 (*)
|    +--- com.vmware.gemfire:gemfire-version:10.1.2
|    |    \--- com.vmware.gemfire:gemfire-common:10.1.2 (*)
|    +--- com.sun.istack:istack-commons-runtime:4.0.1 -> 4.1.2
|    \--- com.vmware.gemfire:gemfire-deployment-chained-classloader:10.1.2
+--- com.vmware.gemfire:gemfire-cq:10.1.2
|    +--- com.vmware.gemfire:gemfire-core:10.1.2 (*)
|    +--- com.vmware.gemfire:gemfire-logging:10.1.2 (*)
|    +--- com.vmware.gemfire:gemfire-membership:10.1.2 (*)
|    +--- com.vmware.gemfire:gemfire-tcp-messenger:10.1.2 (*)
|    \--- com.vmware.gemfire:gemfire-serialization:10.1.2 (*)
+--- com.vmware.gemfire:spring-boot-3.3-gemfire-10.1:2.0.3
|    +--- org.springframework.boot:spring-boot-starter:3.3.6 -> 3.3.10 (*)
|    +--- com.vmware.gemfire:spring-boot-3.3-gemfire-core-10.1:2.0.3
|    |    +--- com.vmware.gemfire:spring-boot-3.3-gemfire-extensions-10.1:2.0.3
|    |    |    +--- org.springframework:spring-web:6.1.15 -> 6.1.18 (*)
|    |    |    \--- com.fasterxml.jackson.core:jackson-databind:2.17.3 (*)
|    |    +--- org.springframework:spring-context-support:6.1.15 -> 6.1.18
|    |    |    +--- org.springframework:spring-beans:6.1.18 (*)
|    |    |    +--- org.springframework:spring-context:6.1.18 (*)
|    |    |    \--- org.springframework:spring-core:6.1.18 (*)
|    |    +--- org.springframework:spring-jcl:6.1.15 -> 6.1.18
|    |    +--- org.springframework.boot:spring-boot-starter:3.3.6 -> 3.3.10 (*)
|    |    +--- com.vmware.gemfire:spring-data-3.3-gemfire-10.1:2.0.2
|    |    |    +--- org.springframework:spring-context-support:6.1.13 -> 6.1.18 (*)
|    |    |    +--- org.springframework:spring-tx:6.1.13 -> 6.1.18 (*)
|    |    |    +--- org.springframework:spring-web:6.1.13 -> 6.1.18 (*)
|    |    |    +--- org.springframework.data:spring-data-commons:3.3.4 -> 3.3.10 (*)
|    |    |    +--- javax.cache:cache-api:1.1.1
|    |    |    +--- org.apache.shiro:shiro-spring:1.13.0
|    |    |    |    +--- org.apache.shiro:shiro-core:1.13.0 (*)
|    |    |    |    \--- org.apache.shiro:shiro-web:1.13.0
|    |    |    |         +--- org.apache.shiro:shiro-core:1.13.0 (*)
|    |    |    |         \--- org.owasp.encoder:encoder:1.2.3
|    |    |    +--- org.aspectj:aspectjweaver:1.9.20.1 -> 1.9.23
|    |    |    +--- com.fasterxml.jackson.core:jackson-annotations:2.16.2 -> 2.17.3 (*)
|    |    |    +--- com.fasterxml.jackson.core:jackson-databind:2.16.2 -> 2.17.3 (*)
|    |    |    \--- antlr:antlr:2.7.7
|    |    +--- org.springframework:spring-test:6.1.15 -> 6.1.18
|    |    |    \--- org.springframework:spring-core:6.1.18 (*)
|    |    \--- com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.3 (*)
|    +--- com.vmware.gemfire:spring-boot-3.3-gemfire-autoconfigure-10.1:2.0.3
|    |    +--- com.vmware.gemfire:spring-boot-3.3-gemfire-core-10.1:2.0.3 (*)
|    |    +--- com.vmware.gemfire:spring-boot-3.3-gemfire-extensions-10.1:2.0.3 (*)
|    |    +--- jakarta.annotation:jakarta.annotation-api:2.1.1
|    |    \--- org.aspectj:aspectjtools:1.9.19 -> 1.9.23
|    \--- org.springframework.shell:spring-shell:1.2.0.RELEASE
|         +--- com.google.guava:guava:17.0
|         +--- jline:jline:2.12
|         +--- org.springframework:spring-context-support:4.2.4.RELEASE -> 6.1.18 (*)
|         +--- commons-io:commons-io:2.4 -> 2.18.0
|         \--- org.springframework:spring-core:4.2.4.RELEASE -> 6.1.18 (*)
+--- org.liquibase:liquibase-core -> 4.27.0
|    +--- com.opencsv:opencsv:5.9 -> 5.11 (*)
|    +--- org.apache.commons:commons-text:1.11.0 -> 1.13.0 (*)
|    +--- org.apache.commons:commons-collections4:4.4
|    +--- org.yaml:snakeyaml:2.2
|    +--- javax.xml.bind:jaxb-api:2.3.1 (*)
|    \--- org.apache.commons:commons-lang3:3.14.0
+--- net.javacrumbs.shedlock:shedlock-spring:5.9.0
|    +--- net.javacrumbs.shedlock:shedlock-core:5.9.0
|    |    \--- org.slf4j:slf4j-api:2.0.9 -> 2.0.17
|    \--- org.springframework:spring-context:6.0.13 -> 6.1.18 (*)
+--- net.javacrumbs.shedlock:shedlock-provider-jdbc-template:5.9.0
|    +--- net.javacrumbs.shedlock:shedlock-core:5.9.0 (*)
|    \--- org.springframework:spring-jdbc:6.0.13 -> 6.1.18 (*)
+--- com.jcraft:jsch:0.1.55
+--- org.apache.sshd:sshd-core:2.11.0
|    +--- org.apache.sshd:sshd-common:2.11.0
|    |    +--- org.slf4j:slf4j-api:1.7.32 -> 2.0.17
|    |    \--- org.slf4j:jcl-over-slf4j:1.7.32 -> 2.0.9 (*)
|    +--- org.slf4j:slf4j-api:1.7.32 -> 2.0.17
|    \--- org.slf4j:jcl-over-slf4j:1.7.32 -> 2.0.9 (*)
+--- org.apache.sshd:sshd-sftp:2.11.0
|    +--- org.apache.sshd:sshd-core:2.11.0 (*)
|    +--- org.slf4j:slf4j-api:1.7.32 -> 2.0.17
|    \--- org.slf4j:jcl-over-slf4j:1.7.32 -> 2.0.9 (*)
\--- org.antlr:antlr4-runtime:4.9.3
