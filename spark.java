import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.SparkEnv;
import org.apache.spark.api.java.function.*;
import static org.apache.spark.sql.functions.*;

public class ParallelizedCsvDbReconciliation {
    
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        // STEP 1: Configure Spark for Kubernetes with Parallelization
        SparkSession sparkSession = SparkSession.builder()
            .appName("ParallelizedCsvDbReconciliation")
            // Remove local[*] - Kubernetes will handle distribution
            .config("spark.sql.adaptive.enabled", "true")
            .config("spark.sql.adaptive.coalescePartitions.enabled", "true")
            .config("spark.sql.adaptive.skewJoin.enabled", "true")
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            .getOrCreate();
        
        // STEP 2: Display Parallelization Information
        System.out.println("=== PARALLELIZATION SETUP ===");
        System.out.println("Default parallelism: " + sparkSession.sparkContext().defaultParallelism());
        System.out.println("Number of executors: " + sparkSession.sparkContext().getExecutorInfos().size());
        
        // STEP 3: Load and Parallelize CSV Dataset
        Dataset<MerchantOrderPayment> datasetRowFile = getParallelizedFileDataset(sparkSession);
        
        // STEP 4: Load and Parallelize DB Dataset  
        Dataset<Row> datasetRowDb = getParallelizedDbDataSet(sparkSession);
        
        // STEP 5: Verify Partitioning
        System.out.println("CSV partitions: " + datasetRowFile.rdd().getNumPartitions());
        System.out.println("DB partitions: " + datasetRowDb.rdd().getNumPartitions());
        
        // STEP 6: Count with Parallelization Verification
        long csvCount = countWithParallelVerification(datasetRowFile, "CSV");
        System.out.println("datasetRowFile.count(): " + csvCount);
        
        // STEP 7: Show DB sample (limit to avoid overwhelming output)
        System.out.println("=== DB DATASET SAMPLE ===");
        datasetRowDb.show(10);
        
        // STEP 8: Execute Parallelized Join with Optimization
        Dataset<Row> matchedWithDB = executeParallelizedJoin(
            datasetRowFile, 
            datasetRowDb, 
            sparkSession
        );
        
        // STEP 9: Display Results
        System.out.println(formatMillis(System.currentTimeMillis() - startTime) 
            + " time took to calculate matchedWithDB(Parallel Spark JOIN).");
        
        System.out.println("=== MATCHED RECORDS SAMPLE ===");
        matchedWithDB.show(20);
        
        System.out.println("Total matched records: " + matchedWithDB.count());
        
        // STEP 10: Optional - Save Results with Parallelization
        saveResultsInParallel(matchedWithDB);
        
        sparkSession.stop();
    }
    
    // PARALLELIZED CSV LOADING
    private static Dataset<MerchantOrderPayment> getParallelizedFileDataset(SparkSession spark) {
        System.out.println("=== LOADING CSV WITH PARALLELIZATION ===");
        
        Dataset<MerchantOrderPayment> dataset = spark.read()
            .option("header", "true")
            .option("inferSchema", "true")
            .option("multiLine", "true")
            .csv("/path/to/large/csv")
            .as(Encoders.bean(MerchantOrderPayment.class))
            
            // EXPLICIT PARALLELIZATION #1: Filter early to reduce data
            .filter(col("atrnNum").isNotNull() 
                .and(col("atrnNum").notEqual(""))
                .and(col("debitAmt").gt(0)))
            
            // EXPLICIT PARALLELIZATION #2: Repartition by join key
            .repartition(200, col("atrnNum"))  // 200 partitions for better distribution
            
            // EXPLICIT PARALLELIZATION #3: Add processing with executor verification
            .map((MapFunction<MerchantOrderPayment, MerchantOrderPayment>) record -> {
                // This runs in parallel across executors
                String executorId = SparkEnv.get() != null ? SparkEnv.get().executorId() : "driver";
                if (Math.random() < 0.001) { // Log occasionally to avoid spam
                    System.out.println("Processing CSV record on executor: " + executorId);
                }
                // Add any data cleaning/transformation here
                return record;
            }, Encoders.bean(MerchantOrderPayment.class))
            
            // EXPLICIT PARALLELIZATION #4: Cache in memory across executors
            .persist(StorageLevel.MEMORY_AND_DISK_2()); // Replicated for fault tolerance
            
        // Trigger caching with a count operation
        System.out.println("Caching CSV dataset across executors...");
        dataset.count(); // This forces the cache to be populated
        
        return dataset;
    }
    
    // PARALLELIZED DB LOADING
    private static Dataset<Row> getParallelizedDbDataSet(SparkSession spark) {
        System.out.println("=== LOADING DB WITH PARALLELIZATION ===");
        
        // Database connection properties for parallel reading
        java.util.Properties connectionProperties = new java.util.Properties();
        connectionProperties.put("user", "your_username");
        connectionProperties.put("password", "your_password");
        connectionProperties.put("driver", "org.postgresql.Driver");
        
        // PARALLEL DB READ OPTIONS
        Dataset<Row> dataset = spark.read()
            // EXPLICIT PARALLELIZATION #1: Parallel DB connections
            .option("numPartitions", "20")  // 20 parallel DB connections
            .option("partitionColumn", "id")  // Numeric column for partitioning
            .option("lowerBound", "1")
            .option("upperBound", "10000000")  // Adjust based on your data
            .option("fetchsize", "10000")  // Optimize batch size
            .jdbc("jdbc:postgresql://your-db:5432/database", "merchant_transactions", connectionProperties)
            
            // EXPLICIT PARALLELIZATION #2: Filter at DB level (pushdown)
            .filter(col("atrnNum").isNotNull()
                .and(col("status").notEqual("CANCELLED"))
                .and(col("created_date").gt("2023-01-01")))
                
            // EXPLICIT PARALLELIZATION #3: Select only needed columns
            .select("atrnNum", "status", "amount", "created_date", "merchant_id")
            
            // EXPLICIT PARALLELIZATION #4: Repartition by join key to match CSV
            .repartition(200, col("atrnNum"))
            
            // EXPLICIT PARALLELIZATION #5: Add parallel processing verification
            .map((MapFunction<Row, Row>) row -> {
                String executorId = SparkEnv.get() != null ? SparkEnv.get().executorId() : "driver";
                if (Math.random() < 0.001) { // Log occasionally
                    System.out.println("Processing DB record on executor: " + executorId);
                }
                return row;
            }, RowEncoder.apply(new StructType()
                .add("atrnNum", DataTypes.StringType)
                .add("status", DataTypes.StringType)
                .add("amount", DataTypes.DoubleType)
                .add("created_date", DataTypes.TimestampType)
                .add("merchant_id", DataTypes.StringType)))
            
            // EXPLICIT PARALLELIZATION #6: Cache across executors
            .persist(StorageLevel.MEMORY_AND_DISK_2());
            
        // Force caching
        System.out.println("Caching DB dataset across executors...");
        dataset.count();
        
        return dataset;
    }
    
    // PARALLELIZED JOIN EXECUTION
    private static Dataset<Row> executeParallelizedJoin(
            Dataset<MerchantOrderPayment> csvDataset,
            Dataset<Row> dbDataset,
            SparkSession spark) {
        
        System.out.println("=== EXECUTING PARALLELIZED JOIN ===");
        
        // EXPLICIT PARALLELIZATION: Configure join strategy
        long csvCount = csvDataset.count();
        long dbCount = dbDataset.count();
        
        Dataset<Row> result;
        
        if (Math.min(csvCount, dbCount) < 1_000_000) {
            // PARALLELIZATION STRATEGY #1: Broadcast join for smaller dataset
            System.out.println("Using broadcast join for better performance");
            
            if (csvCount < dbCount) {
                result = broadcast(csvDataset).join(
                    dbDataset,
                    csvDataset.col("atrnNum").equalTo(dbDataset.col("atrnNum")),
                    "inner"
                );
            } else {
                result = csvDataset.join(
                    broadcast(dbDataset),
                    csvDataset.col("atrnNum").equalTo(dbDataset.col("atrnNum")),
                    "inner"
                );
            }
        } else {
            // PARALLELIZATION STRATEGY #2: Sort-merge join for large datasets
            System.out.println("Using sort-merge join for large datasets");
            
            result = csvDataset
                .join(dbDataset.hint("shuffle_merge"),
                    csvDataset.col("atrnNum").equalTo(dbDataset.col("atrnNum")),
                    "inner");
        }
        
        // EXPLICIT PARALLELIZATION: Select and optimize result
        Dataset<Row> optimizedResult = result
            .select(
                csvDataset.col("mid"),
                csvDataset.col("orderRefNumber"),
                csvDataset.col("sbiOrderRefNumber"),
                csvDataset.col("atrnNum"),
                csvDataset.col("debitAmt"),
                dbDataset.col("status").as("db_status"),
                dbDataset.col("amount").as("db_amount")
            )
            // EXPLICIT PARALLELIZATION: Coalesce to fewer partitions for output
            .coalesce(50)  // Reduce partitions for better output performance
            
            // EXPLICIT PARALLELIZATION: Add verification of parallel processing
            .map((MapFunction<Row, Row>) row -> {
                String executorId = SparkEnv.get() != null ? SparkEnv.get().executorId() : "driver";
                if (Math.random() < 0.001) {
                    System.out.println("Join result processed on executor: " + executorId);
                }
                return row;
            }, RowEncoder.apply(result.schema()))
            
            // Cache final results for multiple operations
            .persist(StorageLevel.MEMORY_AND_DISK());
            
        return optimizedResult;
    }
    
    // PARALLEL COUNT WITH VERIFICATION
    private static long countWithParallelVerification(Dataset<?> dataset, String datasetName) {
        System.out.println("=== COUNTING " + datasetName + " IN PARALLEL ===");
        
        // Add parallel processing verification during count
        Dataset<?> verifiedDataset = dataset.map((MapFunction<Object, Object>) record -> {
            String executorId = SparkEnv.get() != null ? SparkEnv.get().executorId() : "driver";
            if (Math.random() < 0.0001) { // Very occasional logging
                System.out.println("Counting " + datasetName + " on executor: " + executorId);
            }
            return record;
        }, Encoders.bean(Object.class));
        
        return verifiedDataset.count();
    }
    
    // PARALLEL RESULT SAVING
    private static void saveResultsInParallel(Dataset<Row> results) {
        System.out.println("=== SAVING RESULTS IN PARALLEL ===");
        
        results
            .repartition(10)  // EXPLICIT: Use 10 partitions for parallel writing
            .write()
            .mode(SaveMode.Overwrite)
            .option("maxRecordsPerFile", "100000")  // Control file sizes
            .parquet("/path/to/reconciliation/results");  // Parallel write
            
        System.out.println("Results saved in parallel across 10 partitions");
    }
    
    // UTILITY METHOD
    private static String formatMillis(long millis) {
        return String.format("%.2f seconds", millis / 1000.0);
    }
    }
