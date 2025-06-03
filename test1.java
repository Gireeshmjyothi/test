import java.nio.ByteBuffer;
import java.util.UUID;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.api.java.UDF1;

import static org.apache.spark.sql.functions.callUDF;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.col;

// Register UDF once in your service initialization
public void registerUuidToBytesUdf() {
    spark.udf().register("uuidToBytes", (UDF1<String, byte[]>) uuid -> {
        UUID u = UUID.fromString(uuid);
        byte[] buffer = new byte[16];
        ByteBuffer bb = ByteBuffer.wrap(buffer);
        bb.putLong(u.getMostSignificantBits());
        bb.putLong(u.getLeastSignificantBits());
        return buffer;
    }, DataTypes.BinaryType);
}



public void stageReconStatus(Dataset<Row> dataset, String status) {
    Dataset<Row> staged = dataset
            .withColumn("RFD_ID", callUDF("uuidToBytes", col("RFD_ID")))
            .withColumn("RECON_STATUS", lit(status))
            .dropDuplicates("RFD_ID");

    staged.write()
            .mode(SaveMode.Append)
            .format("jdbc")
            .option("url", jdbcUrl)
            .option("dbtable", "RECON_FILE_DTLS_STAGE")
            .option("user", jdbcUserName)
            .option("password", jdbcPassword)
            .option("driver", jdbcDriver)
            .save();
}
