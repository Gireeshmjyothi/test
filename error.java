// Step-6: Prepare final dataset for staging update.
            logger.info("Step-6: Merging all status-tagged datasets for staging.");
            Dataset<Row> finalReconStatusUpdate = matched
                    .select(RFD_ID, RECON_STATUS)
                    .union(unmatched.select(RFD_ID, RECON_STATUS))
                    .union(duplicate.select(RFD_ID, RECON_STATUS))
                    .withColumn("SESSION_ID", lit(UUID.randomUUID().toString()));
