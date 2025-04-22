import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class AESGcmCsvProcessor {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 32; // 256 bits
    private static final int IV_SIZE = 12; // 96 bits (recommended for GCM)
    private static final int TAG_LENGTH_BIT = 128; // Authentication tag size
    private static final int BUFFER_SIZE = 16 * 1024; // 16KB buffer for efficient IO

    // Create SecretKey from a 32-byte (256-bit) key string
    public static SecretKey getAESKey(String keyText) {
        byte[] keyBytes = keyText.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != AES_KEY_SIZE) {
            throw new IllegalArgumentException("Key must be exactly 32 bytes (256 bits).");
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    // Encrypt CSV file using AES-GCM and write output with IV prepended
    public static void encryptCsv(File inputCsv, File outputEncrypted, SecretKey secretKey) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

        try (FileInputStream fis = new FileInputStream(inputCsv);
             FileOutputStream fos = new FileOutputStream(outputEncrypted)) {

            fos.write(iv); // Prepend IV

            try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    // Decrypt encrypted CSV file using AES-GCM (extract IV from input file)
    public static void decryptCsv(File inputEncrypted, File outputCsv, SecretKey secretKey) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputEncrypted)) {

            byte[] iv = fis.readNBytes(IV_SIZE);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputCsv)) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}

import javax.crypto.SecretKey;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        String keyText = "12345678901234567890123456789012"; // 32-char key (secure & fixed)
        SecretKey key = AESGcmCsvProcessor.getAESKey(keyText);

        File inputCsv = new File("sample.csv");
        File encryptedFile = new File("sample_encrypted.aes");
        File decryptedFile = new File("sample_decrypted.csv");

        // Encrypt CSV
        AESGcmCsvProcessor.encryptCsv(inputCsv, encryptedFile, key);
        System.out.println("✅ CSV encrypted successfully.");

        // Decrypt CSV
        AESGcmCsvProcessor.decryptCsv(encryptedFile, decryptedFile, key);
        System.out.println("✅ CSV decrypted successfully and ready for S3 upload.");
    }
}
