public static void main(String[] args) {
    String jobId = String.valueOf(UUID.randomUUID());
    MDC.put(CORRELATION, jobId);
    MDC.put(SCENARIO, "ReconSparkAppMain");
    MDC.put(OPERATION, "main");
    String callbackUrl = args.length > 1 ? args[1] : "http://localhost:9097/api/rns/v1/spark/recon-complete-callback";

    logger.info("Input args: {}", Arrays.toString(args));
    UUID rfId;
    if(StringUtils.isAllBlank(args)) {
      logger.info("No rfId provided, args is all blank.");
      callReconCallback(callbackUrl, jobId, FAILED, "Failed recon spark job.");
      return;
    }
    try {
      rfId = UUID.fromString(args[0]);
    } catch (Exception e) {
      logger.error("Invalid input rfId: {}", args[0]);
      callReconCallback(callbackUrl, jobId, FAILED, "Failed recon spark job. No rfId provided");
      return;
    }
    try (var context = new AnnotationConfigApplicationContext(SparkConfig.class)) {
      logger.info("Application Context Created!!!");
      SparkReconProcessingService service = context.getBean(SparkReconProcessingService.class);
      service.reconProcessing(rfId);
      callReconCallback(callbackUrl, jobId, "SUCCESS", "Completed recon spark job successfully.");
      logger.info("ReconSparkApp Completed");
    } catch (Exception ex) {
      logger.error("Exception while the recon process, error message: {}", ex.getMessage());
      callReconCallback(callbackUrl, jobId, FAILED, "Failed recon spark job, error message: " + ex.getMessage());
    }
  }


 | 25/08/03 17:17:05 ERROR ReconSparkAppMain: Exception while the recon process, error message: Error creating bean with name 'sparkDataRepository' defined in URL [jar:file:/F:/spark/epay_operations_service/libs/recon-spark-job-0.1.0.jar!/com/epay/operations/recon/spark/repository/SparkDataRepository.class]: Unsatisfied dependency expressed through constructor parameter 0: Error creating bean with name 'sparkSession' defined in com.epay.operations.recon.spark.config.SparkConfig: Failed to instantiate [org.apache.spark.sql.SparkSession]: Factory method 'sparkSession' threw exception with message: java.net.UnknownHostException: namenode
2025-08-03 17:17:06.809 INFO | com.epay.operations.controller.SparkController:72 | principal=  | scenario= | operation= | correlation= | reconCompleteCallback | Spark Recon Job Completed!!!
