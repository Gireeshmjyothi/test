public class QueryConstants {

    public static final String SELECT_LAST_24_HOURS_TEMPLATE = """
        (SELECT * FROM %s
         WHERE CREATED_DATE BETWEEN %d AND %d) AS TMP
        """;

    public static String getLast24HoursQuery(String table, long currentMillis) {
        long oneDayBefore = currentMillis - 24 * 60 * 60 * 1000;
        return String.format(SELECT_LAST_24_HOURS_TEMPLATE, table, oneDayBefore, currentMillis);
    }
}
