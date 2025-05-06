spark-submit ^ --class com.rajput.SpringbootJspSparkDemoApplication ^ --master spark://10.30.64.27:7077 ^ "C:\Users\v1014352\Downloads\springboot-jsp-spark-demo 2\springboot-jsp-spark-demo\build\libs\springboot-jsp-spark-demo-0.0.1.war"
Error: Failed to load class com.rajput.SpringbootJspSparkDemoApplication.
25/05/06 11:04:34 INFO ShutdownHookManager: Shutdown hook called
25/05/06 11:04:34 INFO ShutdownHookManager: Deleting directory C:\Users\v1014352\AppData\Local\Temp\spark-abb7ecfa-ded2-45c5-ba9f-30961ab20a7e


jar tf springboot-jsp-spark-demo-0.0.1.jar | grep SpringbootJspSparkDemoApplication

jar xf springboot-jsp-spark-demo-0.0.1.jar META-INF/MANIFEST.MF
cat META-INF/MANIFEST.MF

Main-Class: com.rajput.SpringbootJspSparkDemoApplication

  spark-submit ^
  --class com.rajput.SpringbootJspSparkDemoApplication ^
  --master spark://10.30.64.27:7077 ^
  "C:\Users\v1014352\Downloads\springboot-jsp-spark-demo 2\springboot-jsp-spark-demo\build\libs\springboot-jsp-spark-demo-0.0.1.jar"

java -jar springboot-jsp-spark-demo-0.0.1.jar
