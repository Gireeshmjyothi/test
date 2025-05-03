public ResponseDto process() {
        long startTime = System.currentTimeMillis();

        ResponseDto responseDto = new ResponseDto();
        List<UnMatchedDto> unMatchedRecords = new ArrayList<>();

        // Load DB data
//        String query = JdbcQuery.getYesterdayQuery("MERCHANT_ORDER_PAYMENTS", System.currentTimeMillis());
        long currentMillis = System.currentTimeMillis();
        long oneDayBefore = currentMillis - 24 * 60 * 60 * 1000;
        List<MerchantOrderPaymentDto> dbRecords = merchantOrderPaymentDao.getMerchantOrderPayments(1745489357404L, 1745489357404L);
        Map<String, MerchantOrderPaymentDto> dbMap = dbRecords.stream()
                .filter(r -> r.getAtrnNumber() != null)
                .collect(Collectors.toMap(MerchantOrderPaymentDto::getAtrnNumber, Function.identity(), (a, b) -> a));

        // Load file data
        List<MerchantOrderPaymentDto> fileRecords = fileProcessorService.readCSVByJava("data/merchant_orders.csv");
        Map<String, MerchantOrderPaymentDto> fileMap = fileRecords.stream()
                .filter(r -> r.getAtrnNumber() != null)
                .collect(Collectors.toMap(MerchantOrderPaymentDto::getAtrnNumber, Function.identity(), (a, b) -> a));

        responseDto.setFileRecordCount(fileRecords.size());

        // Find matched records (in both DB and file)
        List<MerchantOrderPaymentDto> matched = fileRecords.stream()
                .filter(r -> dbMap.containsKey(r.getAtrnNumber()))
                .collect(Collectors.toList());
        responseDto.setMatchedRecords(matched);

        // Find unmatched records (in file but not in DB)
        List<MerchantOrderPaymentDto> unmatched = fileRecords.stream()
                .filter(r -> !dbMap.containsKey(r.getAtrnNumber()))
                .toList();

        for (MerchantOrderPaymentDto fileRecord : unmatched) {
            UnMatchedDto unMatchedDto = new UnMatchedDto();
            unMatchedDto.setDbMerchantOrderPaymentDto(fileRecord);
            unMatchedRecords.add(unMatchedDto);
        }

        responseDto.setUnMatchedRecords(unMatchedRecords);

        long endTime = System.currentTimeMillis();
        responseDto.setTimeToProcessed(formatMillis(endTime - startTime));
        System.out.println("Spring Matched data count =======>>>> "+ matched.size());
        System.out.println("Spring Unmatched data count =======>>>> "+ unmatched.size());
        System.out.println("Spring boot processed time ======>>>> "+ formatMillis(endTime - startTime));
        return responseDto;
    }
