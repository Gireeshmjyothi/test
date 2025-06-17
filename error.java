CREATE TABLE RECON_STATUS_STAGE (
    RFD_ID RAW(16),
    RECON_STATUS VARCHAR2(20)
);

CREATE INDEX RSS_RFD_ID_IDX ON RECON_STATUS_STAGE(RFD_ID);

Dataset<Row> matchedWithStatus = matched.withColumn("RECON_STATUS", lit("MATCHED"));
Dataset<Row> unmatchedWithStatus = unmatched.withColumn("RECON_STATUS", lit("UNMATCHED"));
Dataset<Row> duplicateWithStatus = reconFileDetailDuplicate.withColumn("RECON_STATUS", lit("DUPLICATE"));

Dataset<Row> finalReconStatusUpdate = matchedWithStatus
    .select("RFD_ID", "RECON_STATUS")
    .union(unmatchedWithStatus.select("RFD_ID", "RECON_STATUS"))
    .union(duplicateWithStatus.select("RFD_ID", "RECON_STATUS"));


public void writeToStagingTable(Dataset<Row> dataset, String tableName) {
    Properties connectionProperties = new Properties();
    connectionProperties.put("user", "YOUR_DB_USER");
    connectionProperties.put("password", "YOUR_DB_PASSWORD");
    connectionProperties.put("driver", "oracle.jdbc.OracleDriver");

    String jdbcUrl = "jdbc:oracle:thin:@//YOUR_HOST:YOUR_PORT/YOUR_SID";

    dataset.write()
        .mode(SaveMode.Overwrite)
        .jdbc(jdbcUrl, tableName, connectionProperties);
}


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReconStatusUpdater {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void updateReconStatusFromStage() {
        String mergeSql = """
            MERGE INTO RECON_FILE_DTLS tgt
            USING RECON_STATUS_STAGE src
            ON (tgt.RFD_ID = src.RFD_ID)
            WHEN MATCHED THEN
              UPDATE SET tgt.RECON_STATUS = src.RECON_STATUS
        """;

        jdbcTemplate.update(mergeSql);
    }

    public void clearStageTable() {
        jdbcTemplate.update("TRUNCATE TABLE RECON_STATUS_STAGE");
    }
}



@Autowired
private DataSource dataSource;

public void mergeReconStatusUpdates() {
    String mergeSQL = """
        MERGE INTO RECON_FILE_DTLS tgt
        USING RECON_STATUS_STAGE stage
        ON (tgt.RFD_ID = stage.RFD_ID)
        WHEN MATCHED THEN
          UPDATE SET tgt.RECON_STATUS = stage.RECON_STATUS
    """;

    try (Connection conn = dataSource.getConnection();
         Statement stmt = conn.createStatement()) {
        stmt.executeUpdate(mergeSQL);
    } catch (SQLException e) {
        log.error("Failed to merge RECON status updates", e);
        throw new RuntimeException(e);
    }
}

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions.*;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;

public class YourSparkJob {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Recon Status Update")
                .getOrCreate();

        // 🔹 Register the UDF right after creating Spark session
        spark.udf().register("uuidToBytes", (UDF1<String, byte[]>) uuidStr -> {
            UUID uuid = UUID.fromString(uuidStr);
            ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
            buffer.putLong(uuid.getMostSignificantBits());
            buffer.putLong(uuid.getLeastSignificantBits());
            return buffer.array();
        }, DataTypes.BinaryType);

        // 🔹 Now proceed with loading your matched/unmatched/duplicate datasets
        Dataset<Row> matchedWithStatus = ...;
        Dataset<Row> unmatchedWithStatus = ...;
        Dataset<Row> duplicateWithStatus = ...;

        // 🔹 Convert UUID string to bytes using the UDF
        Dataset<Row> matchedConverted = matchedWithStatus
            .withColumn("RFD_ID", functions.callUDF("uuidToBytes", col("RFD_ID")))
            .select("RFD_ID", "RECON_STATUS");

        Dataset<Row> unmatchedConverted = unmatchedWithStatus
            .withColumn("RFD_ID", functions.callUDF("uuidToBytes", col("RFD_ID")))
            .select("RFD_ID", "RECON_STATUS");

        Dataset<Row> duplicateConverted = duplicateWithStatus
            .withColumn("RFD_ID", functions.callUDF("uuidToBytes", col("RFD_ID")))
            .select("RFD_ID", "RECON_STATUS");

        Dataset<Row> finalReconStatusUpdate = matchedConverted
            .union(unmatchedConverted)
            .union(duplicateConverted);

        // Then write to JDBC
        finalReconStatusUpdate.write()
            .mode(SaveMode.Overwrite)
            .jdbc("jdbc:oracle:thin:@//YOUR_HOST:YOUR_PORT/YOUR_SID", "RECON_STATUS_STAGE", connectionProperties);
    }
}

