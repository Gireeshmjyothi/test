public ResponseDto process() {
        log.info("🚀 POC Application started via CommandLineRunner!");
        long startTime = System.currentTimeMillis();
        ResponseDto responseDto = new ResponseDto();
        List<UnMatchedDto> unMatchedRecords = new ArrayList<>();
        Dataset<MerchantOrderPayment> datasetRowDb = getDbDataSet();
        Dataset<MerchantOrderPayment> datasetRowFile = getFileDataset();
        log.info("Matched");
        responseDto.setFileRecordCount(datasetRowFile.count());
        var matchedWithDB = datasetRowFile.join(datasetRowDb, datasetRowFile.col("atrnNum").equalTo(datasetRowDb.col("atrnNum")), "inner")
                .select(
                        datasetRowFile.col("mid"),
                        datasetRowFile.col("orderRefNumber"),
                        datasetRowFile.col("sbiOrderRefNumber"),
                        datasetRowFile.col("atrnNum"),
                        datasetRowFile.col("debitAmt")
                );
        matchedWithDB.show();
//        matchedWithDB.collectAsList().forEach(employee -> log.info(String.valueOf(employee)));
        responseDto.setMatchedRecords(matchedWithDB.as(merchantOrderPayment).collectAsList());
        log.info("Unmatched: updated or added records");
        var missMatchedWithDB = datasetRowFile.join(datasetRowDb, datasetRowFile.col("atrnNum").equalTo(datasetRowDb.col("atrnNum")), "leftanti").as(Encoders.bean(MerchantOrderPayment.class));
        missMatchedWithDB.show();
//        responseDto.setUnMatchedRecords(missMatchedWithDB.as(merchantOrderPayment).collectAsList());
        missMatchedWithDB.collectAsList().forEach(fileRow -> {
            log.info("File Record: {}", fileRow);
            Dataset<MerchantOrderPayment> fileRowDs = datasetRowDb.filter("atrnNum = " + fileRow.getAtrnNum());
            UnMatchedDto unMatchedDto = new UnMatchedDto();
            unMatchedDto.setDbMerchantOrderPayment(fileRow);
            if (fileRowDs.count() == 0) {
                log.info("New record: {}", fileRow.getAtrnNum());
                unMatchedDto.setNew(true);
            } else {
                log.info("Updated, DB record: {}", fileRowDs.collectAsList());
            }
            unMatchedRecords.add(unMatchedDto);
        });
//        responseDto.setUnMatchedRecords(unMatchedRecords);
        responseDto.setTimeToProcessed(formatMillis(System.currentTimeMillis() - startTime));
        return responseDto;
    }



@Data
public class ResponseDto {
    private List<MerchantOrderPayment> matchedRecords;
    private List<UnMatchedDto> unMatchedRecords;
    private long fileRecordCount;
    private String timeToProcessed;
}


@Data
public class UnMatchedDto {
    private MerchantOrderPayment fileMerchantOrderPayment;
    private MerchantOrderPayment dbMerchantOrderPayment;
    private boolean isNew;
}
