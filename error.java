private void processMerchantFeeTypeFlat(BigDecimal totalBearableFeeRate, BigDecimal serviceTax, PricingStructure pricingStructure) {
    BigDecimal totalAbsFee = totalBearableFeeRate;
    BigDecimal txCal = multiplyAndPercent(serviceTax, totalAbsFee);

    BigDecimal bearableFlatFee = pricingStructure.getBearableFlatRate();
    BigDecimal bearableFee = bearableFlatFee.compareTo(totalAbsFee) < 0 ? bearableFlatFee : totalAbsFee;
    BigDecimal bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

    BigDecimal merchantBearableAmt = bearableFee;
    BigDecimal merchantBearableServiceTax = bearableFeeST;
    BigDecimal customerBearableAmt = subtract(totalAbsFee, merchantBearableAmt);
    BigDecimal customerBearableServiceTax = subtract(txCal, bearableFeeST);

    logger.info("Merchant Fees: Amount = {}, Service Tax = {}", merchantBearableAmt, merchantBearableServiceTax);
    logger.info("Customer Fees: Amount = {}, Service Tax = {}", customerBearableAmt, customerBearableServiceTax);
}
