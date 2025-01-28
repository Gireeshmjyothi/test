import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class GenericHashUtil {

    /**
     * Generates a hash for any key (single or composite) by concatenating all its fields or the value itself.
     *
     * @param key the key object (single or composite)
     * @param <T> the type of the key
     * @return the generated hash as a String
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     */
    public static <T> String generateHash(T key) throws NoSuchAlgorithmException {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        StringBuilder data = new StringBuilder();

        // Check if the key is a single value type
        if (isSingleValueType(key)) {
            data.append(key.toString());
        } else {
            // Process as composite key
            var fields = key.getClass().getDeclaredFields();

            // Sort fields to ensure consistent hash generation
            Arrays.sort(fields, (f1, f2) -> f1.getName().compareTo(f2.getName()));

            // Concatenate all field values
            try {
                for (var field : fields) {
                    field.setAccessible(true); // Make private fields accessible
                    Object value = field.get(key); // Get the field value

                    if (value != null) {
                        data.append(value.toString()).append("|"); // Append the value with a delimiter
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Error accessing key fields", e);
            }
        }

        // Generate the hash using SHA-256
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = messageDigest.digest(data.toString().getBytes(StandardCharsets.UTF_8));

        // Convert bytes to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Checks if the key is a single value type (String, Integer, Long, etc.).
     *
     * @param key the key object
     * @return true if it's a single value type, false otherwise
     */
    private static boolean isSingleValueType(Object key) {
        return key instanceof String ||
               key instanceof Number ||
               key instanceof Boolean ||
               key instanceof Character;
    }
}
