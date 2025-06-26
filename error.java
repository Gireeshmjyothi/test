private Dataset<Row>[] classifyReconData(Dataset<Row> reconFileDataset, Dataset<Row> transactionDataset) {
        logger.info("Starting classification of recon data...");

        // Step 1: Join recon and transaction on atrnNum and debitAmt to find exact matches
        Dataset<Row> reconWithTxMatch = reconFileDataset
                .join(transactionDataset,
                        reconFileDataset.col("recon." + ATRN_NUM).equalTo(transactionDataset.col(ATRN_NUM))
                                .and(reconFileDataset.col("recon." + DEBIT_AMT).equalTo(transactionDataset.col(DEBIT_AMT))),
                        "left"
                )
                .withColumn("isMatched", transactionDataset.col(ATRN_NUM).isNotNull());

        // Step 2: Mark matched rows
        Dataset<Row> matched = reconWithTxMatch
                .filter(col("isMatched").equalTo(true))
                .drop("isMatched")
                .withColumn(RECON_STATUS_FIELD, lit(RECON_STATUS_MATCHED));

        // Step 3: Identify unmatched/duplicate (where atrnNum exists in transaction, but debitAmt differs OR not in transaction)
        Dataset<Row> remaining = reconFileDataset
                .join(matched.select("recon." + ATRN_NUM, "recon." + DEBIT_AMT),
                        reconFileDataset.col("recon." + ATRN_NUM).equalTo(matched.col("recon." + ATRN_NUM))
                                .and(reconFileDataset.col("recon." + DEBIT_AMT).equalTo(matched.col("recon." + DEBIT_AMT))),
                        "leftanti");

        // Step 4: Apply row_number partitioned by atrnNum to tag first occurrence as unmatched, rest as duplicates
        WindowSpec dupWindow = Window.partitionBy("recon." + ATRN_NUM).orderBy("recon." + DEBIT_AMT); // can use any column to order

        Dataset<Row> remainingWithFlags = remaining
                .withColumn("row_num", row_number().over(dupWindow))
                .withColumn(RECON_STATUS_FIELD,
                        when(col("row_num").equalTo(1), lit(RECON_STATUS_UNMATCHED))
                                .otherwise(lit(RECON_STATUS_DUPLICATE))
                )
                .drop("row_num");

        // Step 5: Combine matched + remainingWithFlags
        Dataset<Row> finalDataset = matched.unionByName(remainingWithFlags);

        // Return matched, duplicate, unmatched
        return new Dataset[]{
                finalDataset.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_MATCHED)),
                finalDataset.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_DUPLICATE)),
                finalDataset.filter(col(RECON_STATUS_FIELD).equalTo(RECON_STATUS_UNMATCHED))
        };



2025-06-26 11:08:51.713 ERROR | com.epay.rns.service.ReconService:124 | principal=  | scenario= | operation= | correlation= | reconProcess | Error during recon process for rfsId 1a6cf13c-df22-4845-a15a-f740e2716015: [COLUMN_ALREADY_EXISTS] The column `atrn_num` already exists. Consider to choose another name or rename the existing column.
org.apache.spark.sql.AnalysisException: [COLUMN_ALREADY_EXISTS] The column `atrn_num` already exists. Consider to choose another name or rename the existing column.
	at org.apache.spark.sql.errors.QueryCompilationErrors$.columnAlreadyExistsError(QueryCompilationErrors.scala:2450)
	at org.apache.spark.sql.util.SchemaUtils$.checkColumnNameDuplication(SchemaUtils.scala:114)
	at org.apache.spark.sql.catalyst.analysis.ResolveUnion$.org$apache$spark$sql$catalyst$analysis$ResolveUnion$$checkColumnNames(ResolveUnion.scala:198)
	at org.apache.spark.sql.catalyst.analysis.ResolveUnion$$anonfun$apply$2.$anonfun$applyOrElse$1(ResolveUnion.scala:210)
	at scala.collection.IndexedSeqOptimized.reduceLeft(IndexedSeqOptimized.scala:60)
	at scala.collection.IndexedSeqOptimized.reduceLeft$(IndexedSeqOptimized.scala:76)
