import java.util.UUID;

public class UuidConverter {
    public static UUID convertStringToUuid(String uuidStrWithoutHyphens) {
        if (uuidStrWithoutHyphens.length() != 32) {
            throw new IllegalArgumentException("Invalid UUID string format");
        }

        String formatted = uuidStrWithoutHyphens.replaceFirst(
            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
            "$1-$2-$3-$4-$5"
        );
        return UUID.fromString(formatted);
    }

    public static void main(String[] args) {
        String uuidStr = "7f9c1f31f3b14e88b9cbe7df2c9e1b2d";
        UUID uuid = convertStringToUuid(uuidStr);
        System.out.println("UUID: " + uuid);
    }
}
