import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;

import java.util.Map;

public Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    SparkSession spark = sparkConfig.sparkSession();
    int headerRowIndex = fileConfigDto.getHeaderRowIndex(); // 1-based
    String delimiter = fileConfigDto.getDelimiter();

    // Step 1: Read raw lines
    Dataset<String> rawLines = spark.read().textFile(path);

    // Step 2: Zip with index to skip metadata/header lines
    JavaRDD<String> filteredLines = rawLines.javaRDD()
        .zipWithIndex()
        .filter(tuple -> tuple._2 >= (headerRowIndex - 1)) // headerRowIndex is 1-based
        .map(tuple -> tuple._1);

    // Step 3: Convert to Dataset<String>
    Dataset<String> validDataLines = spark.createDataset(filteredLines.rdd(), Encoders.STRING());

    // Step 4: Read as CSV (from Dataset<String>)
    Dataset<Row> raw = spark.read()
        .format("csv")
        .option("header", false)  // already skipped header line if any
        .option("delimiter", delimiter)
        .load(validDataLines);

    // Step 5: Map columns (your existing method)
    return mapColumn(raw, fileConfigDto.getMapColumn(), false);
}
