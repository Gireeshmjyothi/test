import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.Map;

public Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    SparkSession spark = sparkConfig.sparkSession();

    int headerRowIndex = fileConfigDto.getHeaderRowIndex(); // 1-based index
    String delimiter = fileConfigDto.getDelimiter();        // e.g. "^" or "|"
    Map<String, Integer> columnMap = fileConfigDto.getMapColumn();

    // Step 1: Read as plain text lines
    Dataset<String> lines = spark.read().textFile(path);

    // Step 2: Skip header/meta lines using zipWithIndex and filter
    JavaRDD<String> filteredRDD = lines.javaRDD()
        .zipWithIndex()
        .filter((Function<Tuple2<String, Long>, Boolean>) tuple -> tuple._2 >= (headerRowIndex - 1))
        .map((Function<Tuple2<String, Long>, String>) Tuple2::_1);

    // Step 3: Convert to CSV using delimiter
    Dataset<Row> raw = spark.read()
        .option("delimiter", delimiter)
        .option("header", false)
        .csv(filteredRDD);

    // Step 4: Map selected columns based on config
    return mapColumn(raw, columnMap, false); // Assuming your `mapColumn` method exists
}
