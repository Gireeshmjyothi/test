    public TransactionResponse<String> invalidateToken() {
        tokenValidator.validateEmptyToken(SecurityContextHolder.getContext().getAuthentication().getCredentials());
        logger.info(" Invalidate Token - Service");
        EPayPrincipal ePayPrincipal = EPayIdentityUtil.getUserPrincipal();
        TokenDto tokenDto = tokenDao.getActiveTokenByMID(ePayPrincipal.getMid(), ePayPrincipal.getToken(),TokenStatus.ACTIVE).orElseThrow(() -> new TransactionException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Active Token")));
        buildTokenDtoForInvalidate(tokenDto);
        tokenDao.saveToken(tokenDto);
        return TransactionResponse.<String>builder().data(List.of("Token invalidated successfully")).status(1).build();
    }

public void validatePaymentPushStatusVerificationRequest(PaymentVerificationRequest paymentVerificationRequest, String mId) {
        errorDtoList.clear();
        logger.info("Validation ARTN Number.");
        checkMandatoryField(paymentVerificationRequest.getAtrnNumber(), "ATRN number");
        checkMandatoryField(mId, "Merchant Id");
        throwIfErrors();
    }


@Data
@Embeddable
public class MerchantPricingRequestDto {

    @Column(name = "MERCHANTID")
    private String mId;

    @Column(name = "PAYMODECODE")
    private String payModeCode;

    @Column(name = "AGGGTWMAPID")
    private String gtwMapsId;

    @Column(name = "PAYPROCTYPE")
    private String payProcType;

    @Transient
    private BigDecimal transactionAmount;

}

@Data
@Entity
@Table(name = "PRICING_VIEW")
public class MerchantPricing {

    @EmbeddedId
    private MerchantPricingRequestDto pricingRequestDto;

    @Column(name = "INSTRUCTIONTYPE")
    private String instructionType;

    @Column(name = "MERCHANTFEE")
    private BigDecimal merchantFee;

    @Column(name = "SLABFROM")
    private BigDecimal slabFrom;

    @Column(name = "SLABTO")
    private BigDecimal slabTo;

    @Column(name = "MERCHANTFEEAPPLICABLE")
    private Character merchantFeeApplicable;

    @Column(name = "MERCHANTFEETYPE")
    private Character merchantFeeType;

    @Column(name = "OTHERFEEAPPLICABLE")
    private Character otherFeeApplicable;

    @Column(name = "OTHERFEETYPE")
    private Character otherFeeType;

    @Column(name = "OTHERFEE")
    private BigDecimal otherFee;

    @Column(name = "GTWFEEAPPLICABLE")
    private Character gtwFeeApplicable;

    @Column(name = "GTWFEETYPE")
    private Character gtwFeeType;

    @Column(name = "GTWFEE")
    private BigDecimal gtwFee;

    @Column(name = "AGGSERVICEFEEAPPLICABLE")
    private Character aggServiceFeeApplicable;

    @Column(name = "AGGSERVICEFEETYPE")
    private Character aggServiceFeeType;

    @Column(name = "AGGSERVICEFEE")
    private BigDecimal aggServiceFee;

    @Column(name = "FEEPROCESSFLAG")
    private Character feeProcessingFlag;

    @Column(name = "SERVICETAX")
    private BigDecimal serviceTax;

    @Column(name = "SERVICETAXTYPE")
    private String serviceTaxType;

    @Column(name = "SERVICETAXID")
    private String serviceTaxId;

    @Column(name = "TXNAPPLICABLE")
    private Character txnApplicable;

    @Column(name = "TRANSACTIONTYPE")
    private String transactionType;

    @Column(name = "BEARABLECOMPONENT")
    private String bearableComponent;

    @Column(name = "BEARABLEENTITY")
    private Character bearableEntity;

    @Column(name = "BEARABLEAMOUNTCUTOFF")
    private BigDecimal bearableAmountCutoff;

    @Column(name = "BEARABLEFLATRATE")
    private BigDecimal bearableFlatRate;

    @Column(name = "BEARABLELIMIT")
    private String bearableLimit;

    @Column(name = "BEARABLEPERCENTAGERATE")
    private BigDecimal bearablePercentageRate;

    @Column(name = "BEARABLETYPE")
    private Character bearableType;

    @Column(name = "TOTALFEERATE")
    private BigDecimal totalFeeRate;

    @Column(name = "PROCESSFLAG")
    private Character processFlag;

}
