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

public class FeeCalculator {

    // Method to process fees based on provided flags and fee types
    public void processTransactionFee(String txnFeeProcessFlag, BearableFeeInfo bearableFeeInfo, 
                                      TransactionDetails txnDetails) {
        BigDecimal bearableFee = BigDecimal.ZERO;
        BigDecimal serviceTax = BigDecimal.ZERO;
        BigDecimal bearableFeeST = BigDecimal.ZERO;
        BigDecimal totalBearableFee = BigDecimal.ZERO;

        // Check if the process flag is 'Hybrid'
        if (txnFeeProcessFlag.equals("Hybrid")) {
            if (bearableFeeInfo.getBearableType().equals("Amount")) {
                if (bearableFeeInfo.getFeeType().equals("Percentage")) {
                    bearableFee = calculatePercentageFee(bearableFeeInfo, txnDetails.getAmount());
                } else if (bearableFeeInfo.getFeeType().equals("Flat")) {
                    bearableFee = bearableFeeInfo.getAmount();
                }

                // Apply merchant or customer as per bearable entity
                if (bearableFeeInfo.getBearableEntity().equals("Merchant")) {
                    bearableFee = applyMerchantFeeLimits(bearableFeeInfo, bearableFee);
                } else if (bearableFeeInfo.getBearableEntity().equals("Customer")) {
                    bearableFee = applyCustomerFeeLimits(bearableFeeInfo, bearableFee);
                }

                // Calculate service tax based on the fee type
                serviceTax = calculateServiceTax(bearableFee, txnDetails.getServiceTaxRate());
                bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

                // Calculate the total bearable fee (Fee + Service Tax)
                totalBearableFee = bearableFee.add(bearableFeeST);
            }
        }
        
        // Other processes or logic can be added here
    }

    // Calculate a fee based on a percentage
    private BigDecimal calculatePercentageFee(BearableFeeInfo bearableFeeInfo, BigDecimal amount) {
        return amount.multiply(bearableFeeInfo.getPercentage()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    // Apply fee limits for the merchant (min/max limit)
    private BigDecimal applyMerchantFeeLimits(BearableFeeInfo bearableFeeInfo, BigDecimal fee) {
        if (fee.compareTo(bearableFeeInfo.getLowerLimit()) < 0) {
            return bearableFeeInfo.getLowerLimit();
        } else if (fee.compareTo(bearableFeeInfo.getHigherLimit()) > 0) {
            return bearableFeeInfo.getHigherLimit();
        }
        return fee;
    }

    // Apply fee limits for the customer (min/max limit)
    private BigDecimal applyCustomerFeeLimits(BearableFeeInfo bearableFeeInfo, BigDecimal fee) {
        if (fee.compareTo(bearableFeeInfo.getLowerLimit()) < 0) {
            return bearableFeeInfo.getLowerLimit();
        } else if (fee.compareTo(bearableFeeInfo.getHigherLimit()) > 0) {
            return bearableFeeInfo.getHigherLimit();
        }
        return fee;
    }

    // Calculate service tax based on the fee
    private BigDecimal calculateServiceTax(BigDecimal bearableFee, BigDecimal serviceTaxRate) {
        return bearableFee.multiply(serviceTaxRate).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    // Multiply and percent utility method
    private BigDecimal multiplyAndPercent(BigDecimal amount, BigDecimal percentage) {
        return amount.multiply(percentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

    // Example of subtract utility method
    private BigDecimal subtract(BigDecimal amount1, BigDecimal amount2) {
        return amount1.subtract(amount2);
    }

    // Other helper methods and classes can be added as needed
}
