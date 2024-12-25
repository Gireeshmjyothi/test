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


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MerchantPricingRequest {

//    @JsonProperty("mId")
    private String mId;

    private BigDecimal transactionAmount;

    private String payModeCode;

    private String gtwMapsId;

    private String payProcType;

    private String atrn;

    private BigDecimal postAmount;


}


{
    "mId": "1000642",
    "payModeCode": "NB",
    "gtwMapsId": "101",
    "payProcType": "ONUS",
    "transactionAmount": "50",
    "atrn": "235gsfgsog",
    "postAmount": "50"
}
