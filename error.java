String mergeSql = String.format(
    "MERGE INTO %s T " +
    "USING %s S " +
    "ON (T.%s = S.%s) " +
    "WHEN MATCHED THEN UPDATE SET T.RECON_STATUS = S.RECON_STATUS",
    targetTableName, stageTableName, keyColumn, keyColumn
);
