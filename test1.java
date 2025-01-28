import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class GenericHashUtil {

    /**
     * Generates a hash for any composite key object by concatenating all its fields.
     *
     * @param compositeKey the composite key object
     * @param <T>          the type of the composite key
     * @return the generated hash as a String
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     */
    public static <T> String generateHash(T compositeKey) throws NoSuchAlgorithmException {
        // Get all declared fields of the composite key class
        var fields = compositeKey.getClass().getDeclaredFields();

        // Sort fields to ensure consistent hash generation
        Arrays.sort(fields, (f1, f2) -> f1.getName().compareTo(f2.getName()));

        // Build a string by concatenating all field values
        StringBuilder data = new StringBuilder();
        try {
            for (var field : fields) {
                field.setAccessible(true); // Make private fields accessible
                Object value = field.get(compositeKey); // Get the field value

                if (value != null) {
                    data.append(value.toString()).append("|"); // Append the value with a delimiter
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing composite key fields", e);
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
}
