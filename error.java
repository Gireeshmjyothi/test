spark.app.name=RnSSparkPOC
spark.master=local[*]

spring.mvc.view.prefix=/WEB-INF/views/
spring.mvc.view.suffix=.jsp
server.servlet.jsp.init-parameters.development=true

spring.datasource.url=jdbc:oracle:thin:@76.322.654.433:2323:epaydbdev1
spring.datasource.username=TEST
spring.datasource.password=TEST
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.liquibase=false
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=30000
spring.datasource.hikari.max-lifetime=2000000


spring.jpa.show-sql=true
spring.jpa.properties.hibernate.show_sql=true

#DB connection
jdbc.url=jdbc:oracle:thin:@76.322.654.433:2323:epaydbdev1
jdbc.userName=TEST
jdbc.password=TEST
jdbc.driver=oracle.jdbc.OracleDriver


#spark.master can be:
#	local[*] for local mode
#	spark://host:port for Spark Standalone cluster
#	yarn for Hadoop YARN cluster
#	k8s:// for Kubernetes
