// Method to calculate total fees and service tax
private void calculateTotalFeeAndServiceTax(BigDecimal merchPostedAmt, BigDecimal totalBearableFeeRate, BigDecimal serviceTax) {
    totalAbsFee = multiplyAndPercent(merchPostedAmt, totalBearableFeeRate);
    totalBearableFee = totalAbsFee;
    totalBearableFeeServiceTax = multiplyAndPercent(totalBearableFee, serviceTax);
    txCal = multiplyAndPercent(serviceTax, totalAbsFee);
}

// Method to handle both merchant and customer bearable entities
private void handleBearableEntity(BigDecimal totalAbsFee, BigDecimal txCal, BigDecimal totalBearableFeeServiceTax, String bearableEntity) {
    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_MERCHANT)) {
        merchantBearableAmt = totalBearableFee;
        merchantBearableServiceTax = totalBearableFeeServiceTax;
        customerBearableAmt = subtract(totalAbsFee, merchantBearableAmt);
        customerBearableServiceTax = subtract(txCal, totalBearableFeeServiceTax);
    } else if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_CUSTOMER)) {
        customerBearableAmt = totalBearableFee;
        customerBearableServiceTax = totalBearableFeeServiceTax;
        merchantBearableAmt = subtract(totalAbsFee, customerBearableAmt);
        merchantBearableServiceTax = subtract(txCal, totalBearableFeeServiceTax);
    }
}

// Method to handle fee calculation for flat fee type
private void handleFlatFee(BigDecimal totalBearableFeeRate, BigDecimal serviceTax) {
    if (totalBearableFeeRate.compareTo(bearableFlatFee) > 0) {
        totalAbsFee = totalBearableFeeRate;
        totalBearableFee = bearableFlatFee;
    } else {
        totalAbsFee = totalBearableFeeRate;
        totalBearableFee = totalAbsFee;
    }
    txCal = multiplyAndPercent(serviceTax, totalAbsFee);
    totalBearableFeeServiceTax = multiplyAndPercent(totalBearableFee, serviceTax);
}

if (validateAndMatch(bearableComponent, ApplicationConstants.BEARABLE_COMPONENT_AMOUNT)) {

    // txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = P
    if (validateAndMatch(merchantFeeType, ApplicationConstants.MERCHANT_FEE_TYPE_PERCENTAGE)) {

        if (bearableCutOffAmt.compareTo(merchPostedAmt) > 0) {
            bearableCutOffAmt = merchPostedAmt;
        }

        // Calculate total fee and service tax for percentage type
        calculateTotalFeeAndServiceTax(merchPostedAmt, totalBearableFeeRate, serviceTax);

        // Handle merchant and customer bearable entities
        handleBearableEntity(totalAbsFee, txCal, totalBearableFeeServiceTax, bearableEntity);
    }

    // txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = F
    if (validateAndMatch(merchantFeeType, ApplicationConstants.MERCHANT_FEE_TYPE_FLAT) && validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_FLAT)) {

        // Handle flat fee calculations
        handleFlatFee(totalBearableFeeRate, serviceTax);

        // Handle merchant and customer bearable entities
        handleBearableEntity(totalAbsFee, txCal, totalBearableFeeServiceTax, bearableEntity);
    }

    postAmount = merchPostedAmt.add(customerBearableAmt).add(customerBearableServiceTax);
}
