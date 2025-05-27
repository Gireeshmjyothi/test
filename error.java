logger.info("Reading merchant order payments from DB.");
        Dataset<Row> merchantOrderPayments = readAndNormalize("MERCHANT_ORDER_PAYMENTS", "CREATED_DATE", startTime, endTime)
                .alias("src");
        merchantOrderPayments.printSchema();
        logger.info("Reading recon file details from DB.");
        Dataset<Row> reconFileDtls = readAndNormalize("RECON_FILE_DTLS", "PAYMENT_DATE", startTime, endTime)
                .alias("tgt");

        reconFileDtls.printSchema();
        // Rename recon file columns to match source
        logger.info("Renaming recon file columns to match source schema.");
        reconFileDtls = renameColumns(reconFileDtls).alias("tgt");

        reconFileDtls.printSchema();
        // Deduplicate both datasets
        logger.info("Deduplicating merchant order payments.");
        Dataset<Row> merchantDeduped = merchantOrderPayments.dropDuplicates().alias("src");

        logger.info("Deduplicating recon file details.");
        Dataset<Row> reconDeduped = reconFileDtls.dropDuplicates().alias("tgt");

        // Join condition on ATRN_NUM
        Column joinCond = merchantDeduped.col("ATRN_NUM").equalTo(reconDeduped.col("ATRN_NUM"))
                .and(merchantDeduped.col("DEBIT_AMT").equalTo(reconDeduped.col("DEBIT_AMT")))
                .and(merchantDeduped.col("PAYMENT_STATUS").equalTo(reconDeduped.col("PAYMENT_STATUS")))
                .and(merchantDeduped.col("BANK_REFERENCE_NUMBER").equalTo(reconDeduped.col("BANK_REFERENCE_NUMBER")));

        // Value match on all mapped fields
        Column valueMatch = joinCond;
        for (String col : columnMapping().keySet()) {
            valueMatch = valueMatch.and(
                    merchantDeduped.col(col).equalTo(reconDeduped.col(col))
            );
        }

        // Matched records
        logger.info("Finding matched records.");
        Dataset<Row> matched = merchantDeduped.join(reconDeduped, valueMatch, "inner")
                .select("src.*"); // Take only source columns to avoid ambiguity

        // Unmatched records from source
        logger.info("Finding unmatched records (source not in target).");
        Dataset<Row> unmatched = merchantDeduped.join(reconDeduped, joinCond, "left_anti");

        logger.info("Finding duplicate records in target.");
        Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM")
                .join(reconFileDtls, "ATRN_NUM")
                .alias("tgt_dup");

        // Log summaries
        logDataset("Matched Rows", matched);
        logDataset("Unmatched Rows", unmatched);
        logDataset("Target Duplicates", targetDuplicates);
