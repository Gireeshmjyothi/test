public void validatePayment(BigDecimal transactionAmount, String mId, String paymodeCode) {
        logger.info("Fetching data from Admin Service : {}, {}", mId, paymodeCode);
        AggMerchantVolumeVelocityResponse paymodeVVL = getPaymodeVVL(mId, paymodeCode);

        if (ObjectUtils.isEmpty(paymodeVVL)) {
            throw new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "PayMode VVL"));
        }

        MerchantOrderSummary merchantOrderSummary = getMerchantOrderSummary(mId, paymodeCode);

        logger.info("Validation Started.");
        if (transactionAmount.compareTo(paymodeVVL.minTxnLimit) < 0 || transactionAmount.compareTo(paymodeVVL.maxTxnLimit) > 0) {

            throw new ValidationException(ErrorConstants.PAYMENT_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_LIMIT_ERROR_MESSAGE);
        }

        if (paymodeVVL.dailyTxnAmountLimit.compareTo(transactionAmount.add(merchantOrderSummary.getDailyTxnAmount())) < 0) {

            throw new ValidationException(ErrorConstants.PAYMENT_DAILY_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_DAILY_LIMIT_ERROR_MESSAGE);
        }

        if (paymodeVVL.weeklyTxnAmountLimit.compareTo(transactionAmount.add(merchantOrderSummary.getWeeklyTxnAmount())) < 0) {

            throw new ValidationException(ErrorConstants.PAYMENT_WEEKLY_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_WEEKLY_LIMIT_ERROR_MESSAGE);
        }

        if (paymodeVVL.monthlyTxnAmountLimit.compareTo(transactionAmount.add(merchantOrderSummary.getMonthlyTxnAmount())) < 0) {

            throw new ValidationException(ErrorConstants.PAYMENT_MONTHLY_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_MONTHLY_LIMIT_ERROR_MESSAGE);
        }

        if (paymodeVVL.quaterlyTxnAmountLimit.compareTo(transactionAmount.add(merchantOrderSummary.getQuarterlyTxnAmount())) < 0) {

            throw new ValidationException(ErrorConstants.PAYMENT_QUARTERLY_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_QUARTERLY_LIMIT_ERROR_MESSAGE);
        }

        if (paymodeVVL.halfYearlyTxnAmountLimit.compareTo(transactionAmount.add(merchantOrderSummary.getHalfyearlyTxnAmount())) < 0) {

            throw new ValidationException(ErrorConstants.PAYMENT_HALFYEARLY_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_HALFYEARLY_LIMIT_ERROR_MESSAGE);
        }

        if (paymodeVVL.annualTxnAmountLimit.compareTo(transactionAmount.add(merchantOrderSummary.getAnnuallyTxnAmount())) < 0) {

            throw new ValidationException(ErrorConstants.PAYMENT_ANNUAL_TRANSACTION_LIMIT_CODE, ErrorConstants.PAYMENT_ANNUAL_LIMIT_ERROR_MESSAGE);
        }
        logger.info("Validation Completed.");

    }

    private MerchantOrderSummary getMerchantOrderSummary(String mId, String paymodeCode) {
        logger.info("Getting Merchant Order Summery from data base by : {},{}", mId, paymodeCode);
        List<MerchantOrderSummary> orderSummary = merchantOrderSummaryDao.getMerchantOrderSummary(mId, paymodeCode);

        if (orderSummary.isEmpty()) {
            throw new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Order Summary"));
        }

        return orderSummary.getFirst();
    }

    private AggMerchantVolumeVelocityResponse getPaymodeVVL(String mId, String paymodeCode) {
        logger.info("Calling geVvlDetails.");
        List<AggMerchantVolumeVelocityResponse> vvlDetailsList = AdminServicesClient.geVvlDetails(mId, webClientbuilder);
        AggMerchantVolumeVelocityResponse paymodeVVL = null;
        logger.info("Adding VVL Details by PayModeCode.");
        for (AggMerchantVolumeVelocityResponse aggMerchantVolumeVelocityResponse : vvlDetailsList) {

            if (aggMerchantVolumeVelocityResponse.paymodeCode.equalsIgnoreCase(paymodeCode)) {
                paymodeVVL = aggMerchantVolumeVelocityResponse;
                break;
            }
        }

        return paymodeVVL;
    }
