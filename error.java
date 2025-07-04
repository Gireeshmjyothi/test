import org.apache.spark.sql.*;
import org.apache.spark.sql.functions.*;
import org.apache.spark.sql.types.*;

import java.io.File;
import java.util.*;

public class FileProcessor {

    public Dataset<Row> processPipeDelimitedFile(SparkSession spark, File file, Map<String, Integer> config) {
        // Step 1: Read text file (one column named "value")
        Dataset<Row> raw = spark.read()
                .format("text")
                .load(file.getAbsolutePath());

        // Step 2: Split the line into array
        Dataset<Row> splitDF = raw.withColumn("fields", functions.split(col("value"), "\\|"));

        // Step 3: Build select columns based on config
        List<Column> selectedCols = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : config.entrySet()) {
            String key = entry.getKey();
            int index = entry.getValue() - 1; // 1-based to 0-based
            selectedCols.add(col("fields").getItem(index).alias(key));
        }

        // Step 4: Return dataset with selected columns
        return splitDF.select(selectedCols.toArray(new Column[0]));
    }
}
