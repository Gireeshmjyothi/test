// Optimized Fee Calculation
BigDecimal merchantFeeAbs = calculateFee(
        pricingStructure.getMerchantFeeApplicable(), 
        pricingStructure.getMerchantFeeType(), 
        pricingStructure.getMerchantFee(), 
        merchPostedAmt
);

BigDecimal otherFeeAbs = calculateFee(
        pricingStructure.getOtherFeeApplicable(), 
        pricingStructure.getOtherFeeType(), 
        pricingStructure.getOtherFee(), 
        merchPostedAmt
);

BigDecimal gtwFeeAbs = calculateFee(
        pricingStructure.getGtwFeeApplicable(), 
        pricingStructure.getGtwFeeType(), 
        pricingStructure.getGtwFee(), 
        merchPostedAmt
);

BigDecimal aggServiceFeeAbs = calculateFee(
        pricingStructure.getAggServiceFeeApplicable(), 
        pricingStructure.getAggServiceFeeType(), 
        pricingStructure.getAggServiceFee(), 
        merchPostedAmt
);

// Helper method for fee calculation
private static BigDecimal calculateFee(Character feeApplicable, Character feeType, BigDecimal fee, BigDecimal merchPostedAmt) {
    if (feeApplicable == 'Y' && feeType == 'P') {
        return multiplyAndPercent(fee, merchPostedAmt);
    }
    return BigDecimal.ZERO;
}
