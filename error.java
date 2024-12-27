public void validatePayment(BigDecimal transactionAmount, String mId, String paymodeCode) {
    logger.info("Fetching data for validation: mId={}, paymodeCode={}", mId, paymodeCode);

    AggMerchantVolumeVelocityResponse paymodeVVL = getPaymodeVVL(mId, paymodeCode)
            .orElseThrow(() -> new ValidationException(
                    ErrorConstants.NOT_FOUND_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "PayMode VVL")
            ));

    MerchantOrderSummary merchantOrderSummary = getMerchantOrderSummary(mId, paymodeCode);

    logger.info("Validation started.");
    validateTransactionLimits(transactionAmount, paymodeVVL, merchantOrderSummary);
    logger.info("Validation completed.");
}

private Optional<AggMerchantVolumeVelocityResponse> getPaymodeVVL(String mId, String paymodeCode) {
    logger.info("Fetching VVL details for mId={}", mId);

    List<AggMerchantVolumeVelocityResponse> vvlDetailsList = AdminServicesClient.geVvlDetails(mId, webClientbuilder);

    return vvlDetailsList.stream()
            .filter(vvl -> vvl.paymodeCode.equalsIgnoreCase(paymodeCode))
            .findFirst();
}

private MerchantOrderSummary getMerchantOrderSummary(String mId, String paymodeCode) {
    logger.info("Fetching Merchant Order Summary from database: mId={}, paymodeCode={}", mId, paymodeCode);

    return merchantOrderSummaryDao.getMerchantOrderSummary(mId, paymodeCode).stream()
            .findFirst()
            .orElseThrow(() -> new ValidationException(
                    ErrorConstants.NOT_FOUND_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Order Summary")
            ));
}

private void validateTransactionLimits(BigDecimal transactionAmount, 
                                       AggMerchantVolumeVelocityResponse paymodeVVL, 
                                       MerchantOrderSummary merchantOrderSummary) {
    validateLimit(transactionAmount, paymodeVVL.minTxnLimit, paymodeVVL.maxTxnLimit, 
            ErrorConstants.PAYMENT_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_LIMIT_ERROR_MESSAGE);

    validateLimit(transactionAmount.add(merchantOrderSummary.getDailyTxnAmount()), 
            paymodeVVL.dailyTxnAmountLimit, 
            ErrorConstants.PAYMENT_DAILY_TRANSACTION_LIMIT_CODE, 
            ErrorConstants.PAYMENT_DAILY_LIMIT_ERROR_MESSAGE);

    validateLimit(transactionAmount.add(merchantOrderSummary.getWeeklyTxnAmount()), 
            paymodeVVL.weeklyTxnAmountLimit, 
            ErrorConstants.PAYMENT_WEEKLY_TRANSACTION_LIMIT_CODE, 
            ErrorConstants.PAYMENT_WEEKLY_LIMIT_ERROR_MESSAGE);

    validateLimit(transactionAmount.add(merchantOrderSummary.getMonthlyTxnAmount()), 
            paymodeVVL.monthlyTxnAmountLimit, 
            ErrorConstants.PAYMENT_MONTHLY_TRANSACTION_LIMIT_CODE, 
            ErrorConstants.PAYMENT_MONTHLY_LIMIT_ERROR_MESSAGE);

    validateLimit(transactionAmount.add(merchantOrderSummary.getQuarterlyTxnAmount()), 
            paymodeVVL.quarterlyTxnAmountLimit, 
            ErrorConstants.PAYMENT_QUARTERLY_TRANSACTION_LIMIT_CODE, 
            ErrorConstants.PAYMENT_QUARTERLY_LIMIT_ERROR_MESSAGE);

    validateLimit(transactionAmount.add(merchantOrderSummary.getHalfYearlyTxnAmount()), 
            paymodeVVL.halfYearlyTxnAmountLimit, 
            ErrorConstants.PAYMENT_HALFYEARLY_TRANSACTION_LIMIT_CODE, 
            ErrorConstants.PAYMENT_HALFYEARLY_LIMIT_ERROR_MESSAGE);

    validateLimit(transactionAmount.add(merchantOrderSummary.getAnnuallyTxnAmount()), 
            paymodeVVL.annualTxnAmountLimit, 
            ErrorConstants.PAYMENT_ANNUAL_TRANSACTION_LIMIT_CODE, 
            ErrorConstants.PAYMENT_ANNUAL_LIMIT_ERROR_MESSAGE);
}

private void validateLimit(BigDecimal amount, BigDecimal limit, String errorCode, String errorMessage) {
    if (limit.compareTo(amount) < 0) {
        throw new ValidationException(errorCode, errorMessage);
    }
}

private void validateLimit(BigDecimal amount, BigDecimal minLimit, BigDecimal maxLimit, String errorCode, String errorMessage) {
    if (amount.compareTo(minLimit) < 0 || amount.compareTo(maxLimit) > 0) {
        throw new ValidationException(errorCode, errorMessage);
    }
}
