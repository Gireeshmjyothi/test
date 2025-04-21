import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;

public class AESGcmCsvProcessor {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int AES_KEY_SIZE = 32; // 256 bits
    private static final int IV_SIZE = 12; // 96 bits (recommended for GCM)
    private static final int TAG_LENGTH_BIT = 128;

    public static SecretKey getAESKey(String keyText) {
        byte[] keyBytes = keyText.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != AES_KEY_SIZE) {
            throw new IllegalArgumentException("Key must be exactly 32 bytes (256 bits).");
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static void encryptCsv(File inputCsv, File outputEncrypted, SecretKey secretKey) throws Exception {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);

        try (FileInputStream fis = new FileInputStream(inputCsv);
             FileOutputStream fos = new FileOutputStream(outputEncrypted)) {

            fos.write(iv); // write IV at the beginning

            try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                fis.transferTo(cos);
            }
        }
    }

    public static void decryptCsv(File inputEncrypted, File outputCsv, SecretKey secretKey) throws Exception {
        try (FileInputStream fis = new FileInputStream(inputEncrypted)) {

            byte[] iv = fis.readNBytes(IV_SIZE);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

            try (CipherInputStream cis = new CipherInputStream(fis, cipher);
                 FileOutputStream fos = new FileOutputStream(outputCsv)) {
                cis.transferTo(fos);
            }
        }
    }
}
import javax.crypto.SecretKey;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        String keyText = "12345678901234567890123456789012"; // 32 chars = 256-bit key

        SecretKey key = AESGcmCsvProcessor.getAESKey(keyText);

        File inputCsv = new File("sample.csv");
        File encryptedFile = new File("sample_encrypted.aes");
        File decryptedFile = new File("sample_decrypted.csv");

        // Encrypt CSV
        AESGcmCsvProcessor.encryptCsv(inputCsv, encryptedFile, key);
        System.out.println("Encryption done.");

        // Decrypt back to CSV
        AESGcmCsvProcessor.decryptCsv(encryptedFile, decryptedFile, key);
        System.out.println("Decryption done.");
    }
}
