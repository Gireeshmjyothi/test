 if (pricingStructure.getMerchantFeeApplicable() == 'Y' && pricingStructure.getMerchantFeeType() == 'P') {

            merchantFeeAbs = multiplyAndPercent(pricingStructure.getMerchantFee(), merchPostedAmt);

        }

        //2 . Other Fee Calculation
        BigDecimal otherFeeAbs = BigDecimal.ZERO;

        if (pricingStructure.getOtherFeeApplicable() == 'Y' && pricingStructure.getOtherFeeType() == 'P') {

            otherFeeAbs = multiplyAndPercent(pricingStructure.getOtherFee(), merchPostedAmt);

        }

        //3 . Gateway Fee Calculation
        BigDecimal gtwFeeAbs = BigDecimal.ZERO;

        if (pricingStructure.getGtwFeeApplicable() == 'Y' && pricingStructure.getGtwFeeType() == 'P') {

            gtwFeeAbs = multiplyAndPercent(pricingStructure.getGtwFee(), merchPostedAmt);

        }

        //4 . Agg Service Fee Calculation
        BigDecimal aggServiceFeeAbs = BigDecimal.ZERO;

        if (pricingStructure.getAggServiceFeeApplicable() == 'Y' && pricingStructure.getAggServiceFeeType() == 'P') {

            aggServiceFeeAbs = multiplyAndPercent(pricingStructure.getAggServiceFee(), merchPostedAmt);

        }
