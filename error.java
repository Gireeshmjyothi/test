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
