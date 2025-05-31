org.apache.spark.sql.catalyst.ExtendedAnalysisException: [TABLE_OR_VIEW_NOT_FOUND] The table or view `RECON_FILE_DTLS` cannot be found. Verify the spelling and correctness of the schema and catalog.
If you did not qualify the name with a schema, verify the current_schema() output, or qualify the name with the correct schema and catalog.
To tolerate the error on drop use DROP VIEW IF EXISTS or DROP TABLE IF EXISTS.; line 1 pos 11;
'MergeIntoTable ('target.ATRN_NUM = 'source.ATRN_NUM), [updateaction(None, assignment('target.RECON_STATUS, 'source.RECON_STATUS))]
