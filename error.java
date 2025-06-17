private Dataset<Row> convertRfdIdToBytes(Dataset<Row> dataset) {
    return dataset.withColumn("RFD_ID", callUDF("uuidToBytes", col("RFD_ID")))
                  .select("RFD_ID", "RECON_STATUS");
}

Dataset<Row> matchedConverted = convertRfdIdToBytes(matchedWithStatus);
Dataset<Row> unmatchedConverted = convertRfdIdToBytes(unmatchedWithStatus);
Dataset<Row> duplicateConverted = convertRfdIdToBytes(duplicateWithStatus);

Dataset<Row> finalReconStatusUpdate = matchedConverted
    .union(unmatchedConverted)
    .union(duplicateConverted);
