Certainly! Here's a Jira Story (with sub-tasks and a README snippet) tailored for setting up an Apache Spark standalone cluster for development or testing purposes.


---

🧾 Main Story (Jira)

📌 Story Title:

Set up Apache Spark standalone cluster environment for application integration


---

📝 Story Description:

Set up and configure an Apache Spark standalone cluster (master and worker nodes) on a Windows machine to support running Spark jobs from the Spring Boot application.

This setup allows developers to simulate a production-like cluster locally, enabling better testing and performance tuning of Spark-based workloads.

The setup should work with Java 21 and be integrated into the Gradle-based Spring Boot project.


---

✅ Acceptance Criteria:

A Spark master and at least one worker node are running locally.

Spark is accessible via the web UI (http://localhost:8080 or 4040/7077 as configured).

The Spring Boot application connects and submits jobs to the standalone Spark master (spark://localhost:7077).

The setup works with Java 21 and Spark 4.x.

Documentation is provided to guide developers on setup and usage.



---

✅ Recommended Sub-Tasks:

1. Download and configure Apache Spark binaries

Download Spark pre-built with Hadoop (e.g., spark-4.0.0-bin-hadoop3).

Set environment variables (SPARK_HOME, JAVA_HOME, PATH).



2. Start standalone Spark master and worker

Use start-master.cmd and start-worker.cmd.

Connect the worker to spark://localhost:7077.



3. Update Spring Boot project to use standalone Spark cluster

Update SparkSession to use master("spark://localhost:7077").



4. Verify job submission to cluster from application

Run a Spark job and validate execution via Spark web UI.



5. Document setup steps in README or wiki

Include instructions to start the cluster and run the app.





---

📄 README Snippet (Apache Spark Standalone Setup)

## 🚀 Apache Spark Standalone Cluster Setup

This project supports integration with an Apache Spark standalone cluster for development.

### ✅ Prerequisites:
- Java 21
- Gradle 8.x
- Windows machine
- Apache Spark 4.0.0 (pre-built with Hadoop)

### 📦 Download Spark:
Download Spark from: https://spark.apache.org/downloads.html  
Use version: `spark-4.0.0-bin-hadoop3`

### 🧰 Setup Environment Variables:
Add the following to system environment variables:
```cmd
SPARK_HOME=C:\path\to\spark-4.0.0-bin-hadoop3
JAVA_HOME=C:\path\to\jdk-21
PATH=%SPARK_HOME%\bin;%JAVA_HOME%\bin;%PATH%

🟢 Start Spark Master and Worker:

Open 2 CMD terminals:

// Start Spark Master
%SPARK_HOME%\sbin\start-master.cmd

// Start Spark Worker (connect to master URL shown in master UI, e.g., spark://localhost:7077)
%SPARK_HOME%\sbin\start-worker.cmd spark://localhost:7077

Visit: http://localhost:8080 (default Spark master UI)

⚙️ Spring Boot Spark Configuration:

@Bean
public SparkSession sparkSession() {
    return SparkSession.builder()
        .appName("StandaloneSparkApp")
        .master("spark://localhost:7077")
        .getOrCreate();
}

🧪 Testing:

Run the Spring Boot app.

Submit a simple job (e.g., read a CSV or process a dataset).

Verify execution via Spark Web UI.


📝 Notes:

This simulates a real cluster for local testing.

Use local[*] if you don’t need multi-process simulation.

You can add more workers on the same or different machines if needed.


---

Let me know if you'd like a ZIP-ready starter Spark project or a shell/batch script to automate the master-worker setup on Windows.



