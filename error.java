import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;

public Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    SparkSession spark = sparkConfig.sparkSession();
    int headerRowIndex = fileConfigDto.getHeaderRowIndex(); // 1-based index
    String delimiter = fileConfigDto.getDelimiter();

    // Step 1: Read the raw file as lines
    Dataset<String> rawLines = spark.read().textFile(path);

    // Step 2: Skip metadata/header lines using zipWithIndex
    JavaRDD<String> filteredLines = rawLines.javaRDD()
        .zipWithIndex()
        .filter(tuple -> tuple._2 >= (headerRowIndex - 1))
        .map(tuple -> tuple._1);

    // Step 3: Parse filtered RDD as CSV using delimiter
    Dataset<Row> raw = spark.read()
        .format("csv")
        .option("header", false)
        .option("delimiter", delimiter)
        .csv(filteredLines);  // <-- This is valid: passing JavaRDD<String>

    // Step 4: Map required columns based on config
    return mapColumn(raw, fileConfigDto.getMapColumn(), false);
            }
