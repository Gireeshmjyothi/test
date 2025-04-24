public ResponseDto process() {
        log.info("🚀 POC Application started via CommandLineRunner!");
        long startTime = System.currentTimeMillis();
        ResponseDto responseDto = new ResponseDto();
        List<UnMatchedDto> unMatchedRecords = new ArrayList<>();
        Dataset<MerchantOrderPayment> datasetRowDb = getDbDataSet();
        Dataset<MerchantOrderPayment> datasetRowFile = getFileDataset();
        log.info("Matched");
        responseDto.setFileRecordCount(datasetRowFile.count());
        var matchedWithDB = datasetRowFile.join(datasetRowDb)
                .select(
                        datasetRowFile.col("atrnNum")
                );
        matchedWithDB.show();
//        matchedWithDB.collectAsList().forEach(employee -> log.info(String.valueOf(employee)));
        responseDto.setMatchedRecords(matchedWithDB.as(merchantOrderPayment).collectAsList());
        log.info("Unmatched: updated or added records");
        var missMatchedWithDB = datasetRowFile.join(datasetRowDb, new String[]{"mid"}, "leftanti").as(Encoders.bean(MerchantOrderPayment.class));
        missMatchedWithDB.show();

        missMatchedWithDB.collectAsList().forEach(fileRow -> {
            log.info("File Record: {}", fileRow);
            Dataset<MerchantOrderPayment> fileRowDs = datasetRowDb.filter("mid = " + fileRow.getMid());
            UnMatchedDto unMatchedDto = new UnMatchedDto();
            unMatchedDto.setDbMerchantOrderPayment(fileRow);
            if (fileRowDs.count() == 0) {
                log.info("New record: {}", fileRow.getMid());
                unMatchedDto.setNew(true);
            } else {
                log.info("Updated, DB record: {}", fileRowDs.collectAsList());
            }
            unMatchedRecords.add(unMatchedDto);
        });
        responseDto.setUnMatchedRecords(unMatchedRecords);
        responseDto.setTimeToProcessed(formatMillis(System.currentTimeMillis() - startTime));
        return responseDto;
    }
