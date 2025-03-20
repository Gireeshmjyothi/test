List<String> atrnNumbers = merchantOrderPaymentList.stream()
    .filter(mop -> TRANSACTION_STATUS_BOOKED.equals(mop.getTransactionStatus()))
    .map(MerchantOrderPaymentDto::getAtrnNumber)
    .toList();
