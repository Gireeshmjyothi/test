responseDto.setMatchedRecords(matchedWithDB.collectAsList());
responseDto.setFileRecordCount(datasetRowFile.count());
responseDto.setUnMatchedRecords(
    missMatchedWithDB.collectAsList().stream().map(fileRow -> {
        UnMatchedDto unMatchedDto = new UnMatchedDto();
        unMatchedDto.setDbMerchantOrderPayment(fileRow); // even though it's unmatched, you're storing file data
        unMatchedDto.setNew(true);
        return unMatchedDto;
    }).collect(Collectors.toList())
);
