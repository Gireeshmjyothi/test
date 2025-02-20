public class EncryptionDecryptionUtil {
    private static final LoggerUtility logger = LoggerFactoryUtility.getLogger(EncryptionDecryptionUtil.class);

    public String decryptRequest(String request, EncryptionKeyDto encryptionKeyDto) {
        logger.info("Decryption request initiated...");
        String mek = getDecryptedMEK(encryptionKeyDto);

        logger.debug("Request object decryption started: {}", request);
        return DecryptionService.decryptValueBySecretKey(decodedValue(mek), request, EncryptionDecryptionAlgo.AES_GCM_NO_PADDING, GCMIvLength.MAXIMUM, GCMTagLength.STANDARD);
    }

    public String encryptRequest(String request, EncryptionKeyDto encryptionKeyDto) {
        logger.info("Encryption request initiated...");
        String mek = getDecryptedMEK(encryptionKeyDto);

        logger.debug("Request object encryption started: {}", request);
        return EncryptionService.encryptValueBySecretKey(decodedValue(mek), request, EncryptionDecryptionAlgo.AES_GCM_NO_PADDING, GCMIvLength.MAXIMUM, GCMTagLength.STANDARD);
    }

    private String getDecryptedMEK(EncryptionKeyDto encryptionKeyDto) {
        String encryptedKEK = DecryptionService.decryptValueBySecretKey(
            decodedValue(encryptionKeyDto.getAek()), 
            encryptionKeyDto.getKek(), 
            EncryptionDecryptionAlgo.AES_GCM_NO_PADDING, 
            GCMIvLength.MAXIMUM, 
            GCMTagLength.STANDARD
        );
        logger.debug("KEK decrypted successfully.");

        String mek = DecryptionService.decryptValueBySecretKey(
            decodedValue(encryptedKEK), 
            encryptionKeyDto.getMek(), 
            EncryptionDecryptionAlgo.AES_GCM_NO_PADDING, 
            GCMIvLength.MAXIMUM, 
            GCMTagLength.STANDARD
        );
        logger.debug("MEK decrypted successfully.");
        return mek;
    }
}
