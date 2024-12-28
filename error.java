 private static boolean validateAndMatch(Character a, Character b) {

        if (a == null || b == null) {
            return false;
        }

        return Character.toUpperCase(a) == Character.toUpperCase(b);

    }

    private static boolean validateAndMatch(String a, String b) {

        if (a == null || b == null) {
            return false;
        }

        return a.equalsIgnoreCase(b);

    }
private static boolean validateAndMatch(Object a, Object b) {
    return Optional.ofNullable(a)
            .flatMap(first -> Optional.ofNullable(b)
                    .map(second -> {
                        if (first instanceof Character && second instanceof Character) {
                            return Character.toUpperCase((Character) first) == Character.toUpperCase((Character) second);
                        }
                        if (first instanceof String && second instanceof String) {
                            return ((String) first).equalsIgnoreCase((String) second);
                        }
                        return false;
                    }))
            .orElse(false);
}
