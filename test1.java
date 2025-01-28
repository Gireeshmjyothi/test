import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class GenericHashUtil {

    /**
     * Generates a hash for any key (single or composite).
     *
     * @param key the key object (single value or composite object)
     * @param <T> the type of the key
     * @return the generated hash as a String
     * @throws NoSuchAlgorithmException if the hashing algorithm is unavailable
     */
    public static <T> String generateHash(T key) throws NoSuchAlgorithmException {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        // Convert the key to a concatenated string representation
        String data = key instanceof String || isPrimitiveOrWrapper(key)
                ? key.toString() // Single key
                : getCompositeKeyString(key); // Composite key

        // Generate the hash using SHA-256
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = messageDigest.digest(data.getBytes(StandardCharsets.UTF_8));

        // Convert bytes to a hexadecimal string
        return bytesToHex(hashBytes);
    }

    /**
     * Checks if the key is a primitive or wrapper type (e.g., Integer, Double, Boolean).
     *
     * @param key the key object
     * @return true if the key is a primitive or wrapper type, false otherwise
     */
    private static boolean isPrimitiveOrWrapper(Object key) {
        return key instanceof Number || key instanceof Boolean || key instanceof Character;
    }

    /**
     * Converts a composite key object into a concatenated string representation by combining its fields.
     *
     * @param key the composite key object
     * @return a concatenated string of the object's field values
     */
    private static String getCompositeKeyString(Object key) {
        StringBuilder data = new StringBuilder();
        Arrays.stream(key.getClass().getDeclaredFields())
                .sorted((f1, f2) -> f1.getName().compareTo(f2.getName())) // Sort fields for consistent hash
                .forEach(field -> {
                    try {
                        field.setAccessible(true); // Allow access to private fields
                        Object value = field.get(key); // Get the field value
                        if (value != null) {
                            data.append(value).append("|"); // Append the value with a delimiter
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Error accessing key fields", e);
                    }
                });
        return data.toString();
    }

    /**
     * Converts an array of bytes into a hexadecimal string.
     *
     * @param bytes the byte array
     * @return the hexadecimal string representation
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
