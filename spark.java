import static org.apache.spark.sql.functions.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public Map<String, Dataset<Row>> findMatchedUnmatchedAndDuplicateReconRecords(
        Dataset<Row> transactionDataset,
        Dataset<Row> reconFileDataset,
        UUID reconFileId) {

    logger.info("Starting classification of recon data...");

    final String reconFieldFormat = "recon.%s";
    final String txnFieldFormat = "txn.%s";

    // Add row_num as surrogate for missing RFD_ID and alias
    Dataset<Row> recon = reconFileDataset
        .withColumn("row_num", monotonically_increasing_id())
        .alias("recon");

    Dataset<Row> txn = transactionDataset.alias("txn");

    // Window partition by ATRN_NUM, DEBIT_AMT order by row_num (instead of RFD_ID)
    logger.info("Step-3.1: Apply row_number to tag first match per (ATRN_NUM, DEBIT_AMT, row_num)");
    WindowSpec matchWindow = Window.partitionBy(
            col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM)),
            col(String.format(reconFieldFormat, OperationsConstant.DEBIT_AMT))
        )
        .orderBy(col(String.format(reconFieldFormat, "row_num")));

    // Step 3.2: Join recon and txn on ATRN_NUM + DEBIT_AMT
    logger.info("Step-3.2: Performing join between recon and transaction data.");
    Dataset<Row> joined = recon.join(txn,
            col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM))
                .equalTo(col(String.format(txnFieldFormat, OperationsConstant.ATRN_NUM)))
                .and(col(String.format(reconFieldFormat, OperationsConstant.DEBIT_AMT))
                .equalTo(col(String.format(txnFieldFormat, OperationsConstant.DEBIT_AMT)))),
            "left_outer")
        .withColumn(OperationsConstant.EXACT_MATCH,
            when(col(String.format(txnFieldFormat, OperationsConstant.ATRN_NUM)).isNotNull(), lit(1)).otherwise(lit(0)))
        .withColumn(OperationsConstant.MATCH_RANK,
            when(col(OperationsConstant.EXACT_MATCH).equalTo(1), row_number().over(matchWindow)))
        // Add merchantId from transactionDataset
        .withColumn(OperationsConstant.MERCHANT_ID, col(String.format(txnFieldFormat, "merchantId")));

    // Step 3.3: Find distinct matched ATRN_NUM
    logger.info("Step-3.3: Find distinct matched ATRN_NUM.");
    Dataset<Row> matchedAtrns = joined.filter(col(OperationsConstant.MATCH_RANK).equalTo(1))
        .select(col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM)).alias(OperationsConstant.MATCHED_ATRN))
        .distinct();

    // Step 3.4: Re-join matched ATRN_NUM to mark duplicates
    logger.info("Step-3.4: Re-join matched ATRN_NUM to mark duplicates");
    Dataset<Row> withMatchedFlag = joined.join(matchedAtrns,
            col(String.format(reconFieldFormat, OperationsConstant.ATRN_NUM))
                .equalTo(col(OperationsConstant.MATCHED_ATRN)),
            "left_outer")
        .withColumn(OperationsConstant.ATRN_MATCHED, col(OperationsConstant.MATCHED_ATRN).isNotNull());

    // Step 3.5: Apply classification logic
    logger.info("Step-3.5: Applying classification logic to tag rows with status.");
    Dataset<Row> finalStatus = withMatchedFlag.withColumn(OperationsConstant.RECON_STATUS,
        when(col(OperationsConstant.EXACT_MATCH).equalTo(1).and(col(OperationsConstant.MATCH_RANK).equalTo(1)), lit(OperationsConstant.RECON_STATUS_MATCHED))
        .when(col(OperationsConstant.EXACT_MATCH).equalTo(1).and(col(OperationsConstant.MATCH_RANK).gt(1)), lit(OperationsConstant.RECON_STATUS_DUPLICATE))
        .when(col(OperationsConstant.EXACT_MATCH).equalTo(0).and(col(OperationsConstant.ATRN_MATCHED).equalTo(true)), lit(OperationsConstant.RECON_STATUS_DUPLICATE))
        .otherwise(lit(OperationsConstant.RECON_STATUS_UNMATCHED)));

    // Step 3.6: Filter Dataset for the matched, unmatched and duplicate Recon Data
    logger.info("Step-3.6: Filter Dataset for the matched, unmatched and duplicate Recon Data");
    Dataset<Row> matched = finalStatus
        .filter(col(OperationsConstant.RECON_STATUS).equalTo(OperationsConstant.RECON_STATUS_MATCHED))
        .withColumn(OperationsConstant.RECON_STATUS, lit(OperationsConstant.RECON_STATUS_MATCHED))
        .withColumn(OperationsConstant.SETTLEMENT_STATUS,
            when(col(OperationsConstant.SETTLEMENT_STATUS).isNull()
                .or(col(OperationsConstant.SETTLEMENT_STATUS).equalTo(Status.PENDING.name())),
                lit(OperationsConstant.SETTLEMENT_STATUS_SETTLED)))
        .withColumn(OperationsConstant.RF_ID, lit(reconFileId.toString()))
        .select(
            col("row_num").alias(OperationsConstant.RFD_ID),
            col(OperationsConstant.RECON_STATUS),
            col(OperationsConstant.SETTLEMENT_STATUS),
            col(OperationsConstant.RF_ID),
            col(OperationsConstant.MERCHANT_ID)
        );

    Dataset<Row> unmatchedOrDuplicate = finalStatus
        .filter(col(OperationsConstant.RECON_STATUS).isin(OperationsConstant.RECON_STATUS_UNMATCHED, OperationsConstant.RECON_STATUS_DUPLICATE))
        .withColumn(OperationsConstant.RF_ID, lit(reconFileId.toString()))
        .select(
            col("row_num").alias(OperationsConstant.RFD_ID),
            col(OperationsConstant.RECON_STATUS),
            col(OperationsConstant.RF_ID),
            col(OperationsConstant.MERCHANT_ID)
        );

    // Step 3.7: Return results as Map
    Map<String, Dataset<Row>> result = new HashMap<>();
    result.put(OperationsConstant.RECON_STATUS_MATCHED, matched);
    result.put(OperationsConstant.OTHERS, unmatchedOrDuplicate);

    return result;
                }                                functions.lit(OperationsConstant.SETTLEMENT_STATUS_SETTLED)))
                .withColumn(OperationsConstant.RF_ID, functions.lit(String.valueOf(reconFileId)))
                .select(OperationsConstant.RFD_ID, OperationsConstant.RECON_STATUS, OperationsConstant.SETTLEMENT_STATUS, OperationsConstant.RF_ID);

        Dataset<Row> unmatchedOrDuplicate = finalStatus
                .filter(functions.col(OperationsConstant.RECON_STATUS).isin(OperationsConstant.RECON_STATUS_UNMATCHED, OperationsConstant.RECON_STATUS_DUPLICATE))
                .withColumn(OperationsConstant.RF_ID, functions.lit(String.valueOf(reconFileId)))
                .select(OperationsConstant.RFD_ID, OperationsConstant.RECON_STATUS, OperationsConstant.RF_ID);

        // Step 3.7: Return as Map data.
        Map<String, Dataset<Row>> result = new HashMap<>();
        result.put(OperationsConstant.RECON_STATUS_MATCHED, matched);
        result.put(OperationsConstant.OTHERS, unmatchedOrDuplicate);

        return result;
    }
