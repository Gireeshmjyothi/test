
if (validateAndMatch(bearableComponent, ApplicationConstants.BEARABLE_COMPONENT_AMOUNT)) {

                //txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = P
                if (validateAndMatch(merchantFeeType, ApplicationConstants.MERCHANT_FEE_TYPE_PERCENTAGE)) {

                    if (bearableCutOffAmt.compareTo(merchPostedAmt) > 0) {

                        bearableCutOffAmt = merchPostedAmt;
                    }

                    totalAbsFee = multiplyAndPercent(merchPostedAmt, totalBearableFeeRate);

                    totalBearableFee = totalAbsFee;

                    totalBearableFeeServiceTax = multiplyAndPercent(totalBearableFee, serviceTax);

                    txCal = multiplyAndPercent(serviceTax, totalAbsFee);

                    //txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = P & bearableEntity = M
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_MERCHANT)) {

                        merchantBearableAmt = totalBearableFee;
                        merchantBearableServiceTax = totalBearableFeeServiceTax;
                        customerBearableAmt = subtract(totalAbsFee, merchantBearableAmt);
                        customerBearableServiceTax = subtract(txCal, totalBearableFeeServiceTax);

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = P & bearableEntity = C
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_CUSTOMER)) {

                        customerBearableAmt = totalBearableFee;
                        customerBearableServiceTax = totalBearableFeeServiceTax;
                        merchantBearableAmt = subtract(totalAbsFee, customerBearableAmt);
                        merchantBearableServiceTax = subtract(txCal, totalBearableFeeServiceTax);

                    }


                }

                //txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = F
                if (validateAndMatch(merchantFeeType, ApplicationConstants.MERCHANT_FEE_TYPE_FLAT) && validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_FLAT)) {

                    if (totalBearableFeeRate.compareTo(bearableFlatFee) > 0) {

                        totalAbsFee = totalBearableFeeRate;
                        txCal = multiplyAndPercent(serviceTax, totalAbsFee);
                        totalBearableFee = bearableFlatFee;
                        totalBearableFeeServiceTax = multiplyAndPercent(totalBearableFee, serviceTax);

                    } else {

                        totalAbsFee = totalBearableFeeRate;
                        txCal = multiplyAndPercent(serviceTax, totalAbsFee);
                        totalBearableFee = totalAbsFee;
                        totalBearableFeeServiceTax = txCal;
                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = F & bearableEntity = M
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_MERCHANT)) {

                        merchantBearableAmt = totalBearableFee;
                        merchantBearableServiceTax = totalBearableFeeServiceTax;
                        customerBearableAmt = subtract(totalAbsFee, merchantBearableAmt);
                        customerBearableServiceTax = multiplyAndPercent(serviceTax, customerBearableAmt);

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = Amount & merchantFeeType = F & bearableEntity = C
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_CUSTOMER)) {

                        customerBearableAmt = totalBearableFee;
                        customerBearableServiceTax = totalBearableFeeServiceTax;
                        merchantBearableAmt = subtract(totalAbsFee, customerBearableAmt);
                        merchantBearableServiceTax = multiplyAndPercent(serviceTax, merchantBearableAmt);

                    }


                }

                postAmount = merchPostedAmt.add(customerBearableAmt).add(customerBearableServiceTax);

            }
