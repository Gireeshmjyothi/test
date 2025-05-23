spark:
  master: local[*]
  driver:
    extraJavaOptions: "--add-exports=java.base/sun.util.calendar=ALL-UNNAMED"
  executor:
    extraJavaOptions: "--add-exports=java.base/sun.util.calendar=ALL-UNNAMED"



SparkConf sparkConf = new SparkConf()
    .setAppName("YourApp")
    .setMaster("local[*]")
    .set("spark.driver.extraJavaOptions", "--add-exports=java.base/sun.util.calendar=ALL-UNNAMED")
    .set("spark.executor.extraJavaOptions", "--add-exports=java.base/sun.util.calendar=ALL-UNNAMED");

SparkSession spark = SparkSession.builder()
    .config(sparkConf)
    .getOrCreate();
