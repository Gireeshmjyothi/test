// Step 3.6: Filter Dataset for the matched, unmatched, and duplicate Recon Data
Dataset<Row> matched = addStatus(finalStatus.filter(col(RECON_STATUS).equalTo(RECON_STATUS_MATCHED)), RECON_STATUS_MATCHED, SETTLEMENT_STATUS_SETTLED);
Dataset<Row> unmatched = addStatus(finalStatus.filter(col(RECON_STATUS).equalTo(RECON_STATUS_UNMATCHED)), RECON_STATUS_UNMATCHED, Status.PENDING.name());
Dataset<Row> duplicate = addStatus(finalStatus.filter(col(RECON_STATUS).equalTo(RECON_STATUS_DUPLICATE)), RECON_STATUS_DUPLICATE, Status.PENDING.name());

// Add RFS_ID column
matched = matched.withColumn("RFS_ID", lit(String.valueOf(reconFileId)));
unmatched = unmatched.withColumn("RFS_ID", lit(String.valueOf(reconFileId)));
duplicate = duplicate.withColumn("RFS_ID", lit(String.valueOf(reconFileId)));

// Return both datasets or store them in a Map
Map<String, Dataset<Row>> result = new HashMap<>();
result.put("matched", matched.select(RFD_ID, RECON_STATUS, SETTLEMENT_STATUS, "RFS_ID"));
result.put("others", unmatched.union(duplicate).select(RFD_ID, RECON_STATUS, "RFS_ID"));
return result;


public void updateReconStatusFromStage() {
    // For matched → update both
    String mergeMatched = """
        MERGE INTO RECON_FILE_DTLS tgt
        USING RECON_STATUS_STAGE_MATCHED src
        ON (tgt.RFD_ID = src.RFD_ID)
        WHEN MATCHED THEN
          UPDATE SET tgt.RECON_STATUS = src.RECON_STATUS,
                     tgt.SETTLEMENT_STATUS = src.SETTLEMENT_STATUS
    """;

    // For unmatched and duplicate → update only RECON_STATUS
    String mergeOthers = """
        MERGE INTO RECON_FILE_DTLS tgt
        USING RECON_STATUS_STAGE_OTHERS src
        ON (tgt.RFD_ID = src.RFD_ID)
        WHEN MATCHED THEN
          UPDATE SET tgt.RECON_STATUS = src.RECON_STATUS
    """;

    jdbcTemplate.update(mergeMatched);
    jdbcTemplate.update(mergeOthers);
}


Map<String, Dataset<Row>> classified = findMatchedUnmatchedAndDuplicateReconRecords(...);
writeToStagingTable(classified.get("matched"), "RECON_STATUS_STAGE_MATCHED");
writeToStagingTable(classified.get("others"), "RECON_STATUS_STAGE_OTHERS");
updateReconStatusFromStage();
