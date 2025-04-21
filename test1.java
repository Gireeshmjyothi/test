import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;

public class AESCsvProcessor {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int AES_KEY_SIZE = 32; // 256 bits = 32 bytes
    private static final int IV_SIZE = 16;

    public static SecretKey getAESKey(String keyText) {
        byte[] keyBytes = keyText.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != AES_KEY_SIZE) {
            throw new IllegalArgumentException("Key must be exactly 32 bytes (256 bits) long.");
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static void encryptCsv(File inputCsv, File outputEncrypted, SecretKey secretKey) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        try (FileInputStream fis = new FileInputStream(inputCsv);
             FileOutputStream fos = new FileOutputStream(outputEncrypted)) {

            fos.write(iv); // Write IV at the beginning

            try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                fis.transferTo(cos);
            }
        }
    }

    public static void decryptCsv(File inputEncrypted, File outputCsv, SecretKey secretKey) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputEncrypted)) {
            byte[] iv = fis.readNBytes(IV_SIZE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputCsv)) {
                cis.transferTo(fos);
            }
        }
    }
}
