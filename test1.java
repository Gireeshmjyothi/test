import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AESCsvProcessor {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int AES_KEY_SIZE = 32; // 256 bits
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

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        try (FileInputStream fis = new FileInputStream(inputCsv);
             FileOutputStream fos = new FileOutputStream(outputEncrypted)) {

            fos.write(iv); // Save IV for decryption

            try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                fis.transferTo(cos);
            }
        }
    }

    public static void decryptCsv(File inputEncrypted, File outputCsv, SecretKey secretKey) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputEncrypted)) {
            byte[] iv = fis.readNBytes(IV_SIZE);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputCsv)) {
                cis.transferTo(fos);
            }
        }
    }
}

import javax.crypto.SecretKey;
import java.io.File;

public class AESCsvProcessorTest {
    public static void main(String[] args) {
        try {
            // Your 256-bit AES key (must be 32 characters)
            String aesKey = "0123456789ABCDEF0123456789ABCDEF"; // 32 chars

            // Prepare files
            File originalCsv = new File("example.csv");
            File encryptedFile = new File("example.csv.enc");
            File decryptedCsv = new File("example_decrypted.csv");

            // Get key
            SecretKey secretKey = AESCsvProcessor.getAESKey(aesKey);

            // Encrypt
            System.out.println("Encrypting CSV...");
            AESCsvProcessor.encryptCsv(originalCsv, encryptedFile, secretKey);
            System.out.println("Encryption complete: " + encryptedFile.getAbsolutePath());

            // Decrypt
            System.out.println("Decrypting CSV...");
            AESCsvProcessor.decryptCsv(encryptedFile, decryptedCsv, secretKey);
            System.out.println("Decryption complete: " + decryptedCsv.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
