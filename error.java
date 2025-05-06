spark-submit ^ --class com.rajput.SpringbootJspSparkDemoApplication ^ --master spark://10.30.64.27.7077 ^ "E:\Gireesh_M\springboot-jsp-spark-demo 2\springboot-jsp-spark-demo\build\libs\springboot-jsp-spark-demo-0.0.1-thin.jar"
Exception in thread "main" java.lang.NoClassDefFoundError: org/springframework/boot/SpringApplication
        at com.rajput.SpringbootJspSparkDemoApplication.main(SpringbootJspSparkDemoApplication.java:9)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:75)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:52)
        at java.base/java.lang.reflect.Method.invoke(Method.java:580)
        at org.apache.spark.deploy.JavaMainApplication.start(SparkApplication.scala:52)
        at org.apache.spark.deploy.SparkSubmit.org$apache$spark$deploy$SparkSubmit$$runMain(SparkSubmit.scala:1034)
        at org.apache.spark.deploy.SparkSubmit.doRunMain$1(SparkSubmit.scala:199)
        at org.apache.spark.deploy.SparkSubmit.submit(SparkSubmit.scala:222)
        at org.apache.spark.deploy.SparkSubmit.doSubmit(SparkSubmit.scala:91)
        at org.apache.spark.deploy.SparkSubmit$$anon$2.doSubmit(SparkSubmit.scala:1125)
        at org.apache.spark.deploy.SparkSubmit$.main(SparkSubmit.scala:1134)
        at org.apache.spark.deploy.SparkSubmit.main(SparkSubmit.scala)
Caused by: java.lang.ClassNotFoundException: org.springframework.boot.SpringApplication
        at java.base/java.net.URLClassLoader.findClass(URLClassLoader.java:445)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:593)
        at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:526)
