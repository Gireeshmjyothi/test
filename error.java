import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;
import java.util.Map;

public Dataset<Row> loadTxt(String path, FileConfigDto fileConfigDto) {
    SparkSession spark = sparkConfig.sparkSession();
    int headerRowIndex = fileConfigDto.getHeaderRowIndex(); // e.g., 2 means skip 1 line
    String delimiter = fileConfigDto.getDelimiter();        // e.g., "^"
    Map<String, Integer> columnMap = fileConfigDto.getMapColumn();

    // Step 1: Read the file as Dataset<String>
    Dataset<String> lines = spark.read().textFile(path);

    // Step 2: Convert to JavaRDD and skip initial rows
    JavaRDD<String> filteredRDD = lines.javaRDD()
        .zipWithIndex()
        .filter(tuple -> tuple._2 >= (headerRowIndex - 1)) // 1-based index
        .map(Tuple2::_1);

    // Step 3: Convert back to Dataset<String>
    Dataset<String> validLines = spark.createDataset(filteredRDD.rdd(), Encoders.STRING());

    // Step 4: Parse as CSV with delimiter
    Dataset<Row> raw = spark.read()
        .option("delimiter", delimiter)
        .option("header", false)
        .csv(validLines);

    // Step 5: Map columns using your config
    return mapColumn(raw, columnMap, false);
}
