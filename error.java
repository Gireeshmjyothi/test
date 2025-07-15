@Component
public class AESFileEncryptionUtil {

    private static final int IV_LENGTH = 16;
    private static final String SALT = "FixedSaltValue123"; // You can also make this a param

    private SecretKeySpec getSecretKeySpec(String secretKey) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), SALT.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    public byte[] encrypt(byte[] data, String secretKey) {
        try {
            SecretKeySpec keySpec = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(data);

            byte[] result = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, result, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, result, IV_LENGTH, encrypted.length);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public byte[] decrypt(byte[] encryptedData, String secretKey) {
        try {
            SecretKeySpec keySpec = getSecretKeySpec(secretKey);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(encryptedData, 0, iv, 0, IV_LENGTH);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            byte[] actualEncrypted = new byte[encryptedData.length - IV_LENGTH];
            System.arraycopy(encryptedData, IV_LENGTH, actualEncrypted, 0, actualEncrypted.length);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(actualEncrypted);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
