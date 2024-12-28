BigDecimal txCal;
        BigDecimal postAmount = BigDecimal.ZERO;
        BigDecimal totalAbsFee;
        BigDecimal totalBearableFee;
        BigDecimal totalBearableFeeServiceTax;
        BigDecimal merchantBearableAmt = BigDecimal.ZERO;
        BigDecimal merchantBearableServiceTax = BigDecimal.ZERO;
        BigDecimal customerBearableAmt = BigDecimal.ZERO;
        BigDecimal customerBearableServiceTax = BigDecimal.ZERO;
        BigDecimal totalBearableFlatAmt = BigDecimal.ZERO;
        BigDecimal bearablePercentFee = BigDecimal.ZERO;
        BigDecimal bearableFee = BigDecimal.ZERO;
        BigDecimal bearableFeeST = BigDecimal.ZERO;

        logger.info("Setting Pricing Structure.");
        //Pricing Structure
        Character merchantFeeType = pricingStructure.getMerchantFeeType();
        Character otherFeeApplicable = pricingStructure.getOtherFeeApplicable();
        Character otherFeeType = pricingStructure.getOtherFeeType();
        BigDecimal otherFee = pricingStructure.getOtherFee();
        Character gtwFeeApplicable = pricingStructure.getGtwFeeApplicable();
        Character gtwFeeType = pricingStructure.getGtwFeeType();
        BigDecimal gtwFee = pricingStructure.getGtwFee();
        Character aggServiceFeeApplicable = pricingStructure.getAggServiceFeeApplicable();
        Character aggServiceFeeType = pricingStructure.getAggServiceFeeType();
        BigDecimal aggServiceFee = pricingStructure.getAggServiceFee();
        Character feeProcessingFlag = pricingStructure.getFeeProcessingFlag();
        BigDecimal serviceTax = pricingStructure.getServiceTax();
        String serviceTaxType = pricingStructure.getServiceTaxType();
        String serviceTaxId = pricingStructure.getServiceTaxId();
        Character txnApplicable = pricingStructure.getTxnApplicable();
        String transactionType = pricingStructure.getTransactionType();

        //as per old code
        Character txnFeeProcessFlag = pricingStructure.getFeeProcessingFlag();
        String bearableComponent = pricingStructure.getBearableComponent();
        Character bearableEntity = pricingStructure.getBearableEntity();
        BigDecimal bearableCutOffAmt = pricingStructure.getBearableAmountCutoff();
        BigDecimal bearableFlatFee = pricingStructure.getBearableFlatRate();
        String bearableLimit = pricingStructure.getBearableLimit();
        BigDecimal bearablePercentRate = pricingStructure.getBearablePercentageRate();
        Character bearableType = pricingStructure.getBearableType();
        BigDecimal totalBearableFeeRate = pricingStructure.getTotalFeeRate();

        //txnFeeProcessFlag = Hybrid
        if (validateAndMatch(txnFeeProcessFlag, ApplicationConstants.TRANSACTION_PROCESSING_FLAG_HYBRID)) {

            //txnFeeProcessFlag = Hybrid & bearableComponent = Amount
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

            //txnFeeProcessFlag = Hybrid & bearableComponent = FEE
            if (validateAndMatch(bearableComponent, ApplicationConstants.BEARABLE_COMPONENT_FEE)) {

                //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P
                if (validateAndMatch(merchantFeeType, ApplicationConstants.MERCHANT_FEE_TYPE_PERCENTAGE)) {

                    totalBearableFee = multiplyAndPercent(merchPostedAmt, totalBearableFeeRate);
                    totalBearableFeeServiceTax = multiplyAndPercent(totalBearableFee, serviceTax);
                    txCal = totalBearableFeeServiceTax;
                    totalAbsFee = totalBearableFee;

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableType = B
                    if (validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_BUSINESS)) {

                        if (bearablePercentRate.compareTo(BigDecimal.ZERO) > 0) {

                            bearablePercentFee = multiplyAndPercent(totalBearableFee, bearablePercentRate);
                        }

                        //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableType = B & bearableLimit = LOWER
                        if (validateAndMatch(bearableLimit, ApplicationConstants.BEARABLE_LIMIT_LOWER)) {

                            if (bearablePercentFee.compareTo(bearableFlatFee) < 0) {

                                bearableFee = bearablePercentFee;

                            } else {

                                bearableFee = bearableFlatFee;
                            }

                            bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

                        }

                        //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableType = B & bearableLimit = HIGHER
                        if (validateAndMatch(bearableLimit, ApplicationConstants.BEARABLE_LIMIT_HIGHER)) {

                            if (bearablePercentFee.compareTo(bearableFlatFee) > 0) {

                                bearableFee = bearablePercentFee;

                            } else {

                                bearableFee = bearableFlatFee;
                            }

                            bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

                        }

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableType = P
                    if (validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_PERCENTAGE)) {

                        //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableType = P & bearableEntity = M
                        if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_MERCHANT)) {

                            bearableFee = totalBearableFee;

                        }

                        //txnFeeProcessFlag = Hybrid & bearableComponent =  FEE & merchantFeeType = P & bearableType = P & bearableEntity = C
                        if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_CUSTOMER)) {

                            bearableFee = multiplyAndPercent(totalBearableFee, bearablePercentRate);

                        }

                        bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableType = F
                    if (validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_FLAT)) {

                        if (totalBearableFee.compareTo(bearableFlatFee) < 0) {

                            bearableFee = totalBearableFee;
                            bearableFeeST = totalBearableFeeServiceTax;

                        } else {

                            bearableFee = bearableFlatFee;
                            bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

                        }

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableEntity = M
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_MERCHANT)) {

                        merchantBearableAmt = bearableFee;
                        merchantBearableServiceTax = bearableFeeST;
                        customerBearableAmt = subtract(totalBearableFee, merchantBearableAmt);
                        customerBearableServiceTax = subtract(totalBearableFeeServiceTax, bearableFeeST);

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = P & bearableEntity = C
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_CUSTOMER)) {

                        customerBearableAmt = bearableFee;
                        customerBearableServiceTax = bearableFeeST;
                        merchantBearableAmt = subtract(totalBearableFee, customerBearableAmt);
                        merchantBearableServiceTax = subtract(totalBearableFeeServiceTax, bearableFeeST);

                    }

                }

                //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = F
                if (validateAndMatch(merchantFeeType, ApplicationConstants.MERCHANT_FEE_TYPE_FLAT)) {

                    totalAbsFee = totalBearableFeeRate;
                    txCal = multiplyAndPercent(totalBearableFeeRate, serviceTax);

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = F & bearableType = B
                    if (validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_BUSINESS)) {

                        if (bearablePercentRate.compareTo(BigDecimal.ZERO) > 0) {
                            bearablePercentFee = multiplyAndPercent(totalBearableFeeRate, bearablePercentRate);
                        }

                        //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = F & bearableType = B & bearableLimit = LOWER
                        if (validateAndMatch(bearableLimit, ApplicationConstants.BEARABLE_LIMIT_LOWER)) {

                            if (bearablePercentFee.compareTo(bearableFlatFee) < 0) {

                                bearableFee = bearablePercentFee;

                            } else {

                                bearableFee = bearableFlatFee;
                            }

                        }

                        //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType =  F & bearableType = B & bearableLimit = HIGHER
                        if (validateAndMatch(bearableLimit, ApplicationConstants.BEARABLE_LIMIT_HIGHER)) {

                            if (bearablePercentFee.compareTo(bearableFlatFee) > 0) {

                                bearableFee = bearablePercentFee;

                            } else {

                                bearableFee = bearableFlatFee;
                            }
                        }

                        bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = F & bearableType = P
                    if (validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_PERCENTAGE)) {

                        bearableFee = multiplyAndPercent(totalBearableFeeRate, bearablePercentRate);
                        bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);


                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = F & bearableType = F
                    if (validateAndMatch(bearableType, ApplicationConstants.BEARABLE_TYPE_FLAT)) {

                        if (totalBearableFeeRate.compareTo(bearableFlatFee) < 0) {

                            bearableFee = totalBearableFeeRate;

                        } else {

                            bearableFee = bearableFlatFee;
                        }

                        bearableFeeST = multiplyAndPercent(bearableFee, serviceTax);

                    }

                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = F & bearableEntity = M
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_MERCHANT)) {

                        merchantBearableAmt = bearableFee;
                        merchantBearableServiceTax = bearableFeeST;
                        customerBearableAmt = subtract(totalAbsFee, merchantBearableAmt);
                        customerBearableServiceTax = subtract(txCal, merchantBearableServiceTax);

                    }


                    //txnFeeProcessFlag = Hybrid & bearableComponent = FEE & merchantFeeType = F & bearableEntity = C
                    if (validateAndMatch(bearableEntity, ApplicationConstants.BEARABLE_ENTITY_CUSTOMER)) {

                        customerBearableAmt = bearableFee;
                        customerBearableServiceTax = bearableFeeST;
                        merchantBearableAmt = subtract(totalAbsFee, customerBearableAmt);
                        merchantBearableServiceTax = subtract(txCal, bearableFeeST);

                    }

                }

                postAmount = merchPostedAmt.add(customerBearableAmt).add(customerBearableServiceTax);

            }

        } else {

            throw new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Hybrid transaction processing flag"));
        }


        //if valid atrn is passed in request then store in transaction db: AGG_MERCHANT_ORDER_HYBRID_FEE_DTLS
        if (!StringUtils.isEmpty(merchantPricingRequestDto.getAtrn())) {

            if (postAmount.compareTo(merchantPricingRequestDto.getPostAmount()) != 0) {

                throw new ValidationException(ErrorConstants.INVALID_ERROR_CODE, MessageFormat.format(ErrorConstants.INVALID_ERROR_MESSAGE, "Pricing info", "Post amount does not match."));
            }

            MerchantOrderHybridFee merchantOrderHybridFee = MerchantOrderHybridFee.builder()
                    .atrn(merchantPricingRequestDto.getAtrn())
                    .mId(merchantPricingRequestDto.getMId())
                    .bearableEntity(pricingStructure.getBearableEntity())
                    .bearableComponent(pricingStructure.getBearableComponent())
                    .bearableAmountCutoff(bearableCutOffAmt)
                    .bearableRateType(pricingStructure.getBearableType())
                    .bearablePercentageRate(pricingStructure.getBearablePercentageRate())
                    .bearableFlatRate(pricingStructure.getBearableFlatRate())
                    .merchantFeeBearableAbs(merchantBearableAmt)
                    .customerFeeBearableAbs(customerBearableAmt)
                    .merchantStBearableAbs(merchantBearableServiceTax)
                    .customerStBearableAbs(customerBearableServiceTax)
                    .bearableLimit(pricingStructure.getBearableLimit())
                    .createDate(new Date())
                    .createdBy("1")
                    .createdBySessionId("1")
                    .processFlag(pricingStructure.getProcessFlag())
                    .build();

            merchantOrderHybridFeeRepo.save(merchantOrderHybridFee);
            logger.info("Merchant Order Hybrid Fee saved successfully.");
        }

        logger.info("Building Merchant Fee response.");
        return MerchantFeeDto.builder()
                //Inputs
                .mId(merchantPricingRequestDto.getMId())
                .gtwMapsId(merchantPricingRequestDto.getGtwMapsId())
                .payModeCode(merchantPricingRequestDto.getPayModeCode())
                .payProcType(merchantPricingRequestDto.getPayProcType())
                .merchPostedAmt(merchantPricingRequestDto.getTransactionAmount())
                .atrn(merchantPricingRequestDto.getAtrn())
                //Upper calculations
                .merchantFeeAbs(merchantFeeAbs)
                .otherFeeAbs(otherFeeAbs)
                .gtwFeeAbs(gtwFeeAbs)
                .aggServiceFeeAbs(aggServiceFeeAbs)
                //Main Calculations
                .postAmount(postAmount)
                .customerBearableAmt(customerBearableAmt)
                .customerBearableServiceTax(customerBearableServiceTax)
                .merchantBearableAmt(merchantBearableAmt)
                .merchantBearableServiceTax(merchantBearableServiceTax)
                .build();
    }
