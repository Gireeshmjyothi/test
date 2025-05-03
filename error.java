public ResponseDto process() {
    long startTime = System.currentTimeMillis();

    ResponseDto responseDto = new ResponseDto();
    List<UnMatchedDto> unMatchedRecords = new ArrayList<>();

    // Load DB data
    List<MerchantOrderPaymentDto> dbRecords = merchantOrderPaymentDao
            .getMerchantOrderPayments(1745489357404L, 1745489357404L);

    Map<String, MerchantOrderPaymentDto> dbMap = dbRecords.parallelStream()
            .filter(r -> r.getAtrnNumber() != null)
            .collect(Collectors.toMap(MerchantOrderPaymentDto::getAtrnNumber, Function.identity(), (a, b) -> a));

    // Load file data
    List<MerchantOrderPaymentDto> fileRecords = fileProcessorService.readCSVByJava("data/merchant_orders.csv");

    Map<String, MerchantOrderPaymentDto> fileMap = fileRecords.parallelStream()
            .filter(r -> r.getAtrnNumber() != null)
            .collect(Collectors.toMap(MerchantOrderPaymentDto::getAtrnNumber, Function.identity(), (a, b) -> a));

    responseDto.setFileRecordCount(fileRecords.size());

    // Matched: atrnNumber exists in both
    List<MerchantOrderPaymentDto> matched = fileRecords.parallelStream()
            .filter(r -> r.getAtrnNumber() != null && dbMap.containsKey(r.getAtrnNumber()))
            .collect(Collectors.toList());
    responseDto.setMatchedRecords(matched);

    // Unmatched from file (file → DB)
    List<UnMatchedDto> fileUnmatched = fileRecords.parallelStream()
            .filter(r -> r.getAtrnNumber() != null && !dbMap.containsKey(r.getAtrnNumber()))
            .map(fileRecord -> {
                UnMatchedDto dto = new UnMatchedDto();
                dto.setDbMerchantOrderPaymentDto(fileRecord);
                dto.setSource("FILE_ONLY");
                return dto;
            })
            .collect(Collectors.toList());

    // Unmatched from DB (DB → file)
    List<UnMatchedDto> dbUnmatched = dbRecords.parallelStream()
            .filter(r -> r.getAtrnNumber() != null && !fileMap.containsKey(r.getAtrnNumber()))
            .map(dbRecord -> {
                UnMatchedDto dto = new UnMatchedDto();
                dto.setDbMerchantOrderPaymentDto(dbRecord);
                dto.setSource("DB_ONLY");
                return dto;
            })
            .collect(Collectors.toList());

    // Combine unmatched records from both sources
    unMatchedRecords.addAll(fileUnmatched);
    unMatchedRecords.addAll(dbUnmatched);

    responseDto.setUnMatchedRecords(unMatchedRecords);

    long endTime = System.currentTimeMillis();
    responseDto.setTimeToProcessed(formatMillis(endTime - startTime));

    System.out.println("Matched records: " + matched.size());
    System.out.println("Unmatched records: " + unMatchedRecords.size());
    System.out.println("Processing time: " + formatMillis(endTime - startTime));

    return responseDto;
}
