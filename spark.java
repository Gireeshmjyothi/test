import org.apache.spark.launcher.SparkLauncher;

public class SparkK8sJobSubmitter {

    public static void main(String[] args) throws Exception {
        Process spark = new SparkLauncher()
            .setAppResource("local:///opt/spark-apps/my-spark-job.jar") // inside Docker image
            .setMainClass("com.example.MainJob")
            .setMaster("k8s://https://<K8S-API-SERVER>:6443")
            .setDeployMode("cluster")
            .setConf("spark.executor.instances", "2")
            .setConf("spark.kubernetes.container.image", "my-docker-repo/my-spark-image:latest")
            .setConf("spark.kubernetes.namespace", "spark")
            .setConf("spark.kubernetes.authenticate.driver.serviceAccountName", "spark-sa")
            .addAppArgs("arg1", "arg2")
            .launch();

        // Capture logs
        new Thread(() -> {
            try (var reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(spark.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[Spark Output] " + line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        int exitCode = spark.waitFor();
        System.out.println("Spark job finished with exit code: " + exitCode);
    }
}
