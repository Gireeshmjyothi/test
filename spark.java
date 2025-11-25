import java.util.Arrays;

public enum PaymentStatusCode {

    SUCCESS("SUCCESS", 'S'),
    FAILED("FAILED", 'F'),
    PENDING("PENDING", 'P');

    private final String key;
    private final char code;

    PaymentStatusCode(String key, char code) {
        this.key = key;
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    /**
     * Convert String → Enum using Arrays.stream
     * Throws RuntimeException if not found
     */
    public static PaymentStatusCode fromString(String key) {
        return Arrays.stream(values())
                .filter(status -> status.key.equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> 
                        new RuntimeException("Invalid PaymentStatusCode: " + key)
                );
    }

    /**
     * Shortcut method → return char directly
     */
    public static char getCodeFromString(String key) {
        return fromString(key).getCode();
    }
}
