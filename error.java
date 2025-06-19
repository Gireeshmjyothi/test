Perfect — since you're using Apache Spark for Java development on Windows, here's a clean Spark setup guide for Java-only usage, without Python or PySpark steps.


---

📘 Apache Spark (Java) Setup Guide for Windows

🎯 Objective:

To install and configure Apache Spark for Java-based development on Windows machines. This setup avoids Python and PySpark dependencies and ensures consistency across your team.


---

🧰 Prerequisites

Tool	Recommended Version	Purpose

Java (JDK)	8 or 11	Required by Spark
Spark	3.4.x	Use pre-built with Hadoop
WinUtils	Hadoop binary	Required for Windows environment
IDE	IntelliJ / Eclipse	Java project development
Maven/Gradle	Latest	For managing Spark project builds



---

🔧 Step-by-Step Setup

✅ 1. Install Java JDK

1. Download JDK from Oracle:
https://www.oracle.com/java/technologies/javase-downloads.html


2. Set environment variables:

JAVA_HOME = C:\Program Files\Java\jdk-11.x.x

Add %JAVA_HOME%\bin to your Path.



3. Verify:

java -version




---

✅ 2. Download & Configure Apache Spark

1. Download Spark from:
https://spark.apache.org/downloads
Choose:

Spark version: 3.4.x

Package type: Pre-built for Apache Hadoop 3



2. Extract Spark to:

C:\spark\spark-3.4.1-bin-hadoop3\


3. Set environment variables:

SPARK_HOME = C:\spark\spark-3.4.1-bin-hadoop3

Add %SPARK_HOME%\bin to your Path.





---

✅ 3. Configure WinUtils (for Hadoop on Windows)

1. Download winutils.exe (matching Hadoop version) from:
https://github.com/steveloughran/winutils


2. Place it in:

C:\hadoop\bin\winutils.exe


3. Set:

HADOOP_HOME = C:\hadoop

Add %HADOOP_HOME%\bin to your Path.



4. Create temp directory for Hadoop:

mkdir C:\tmp\hadoop
winutils.exe chmod 777 /tmp/hadoop




---

🧪 Sample Java Spark Project (Using Maven)

Sample pom.xml Dependencies:

<dependencies>
    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-core_2.12</artifactId>
        <version>3.4.1</version>
    </dependency>
</dependencies>

Sample Java Code (Main.java):

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;

public class Main {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("WordCount").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> data = sc.textFile("README.md");
        long count = data.count();
        System.out.println("Total lines: " + count);

        sc.close();
    }
}

> Run this using your IDE or command line with Maven.




---

🔍 Spark Web UI

When you run a Spark job, Spark's Web UI is available at:

http://localhost:4040


---

🧼 Stop Spark Processes

If you run Spark in standalone mode:

%SPARK_HOME%\sbin\stop-master.cmd
%SPARK_HOME%\sbin\stop-worker.cmd


---

✅ Final Setup Checklist

✔ Java installed and in JAVA_HOME
✔ Spark downloaded and in SPARK_HOME
✔ winutils.exe placed in HADOOP_HOME
✔ Environment variables configured
✔ Able to run Java Spark code in IDE


---

Let me know if you want:

A Gradle-based Java Spark setup

A downloadable Markdown or PDF version of this guide

A sample IntelliJ project structure


I can help generate that instantly.

