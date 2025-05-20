(SELECT * FROM MERCHANT_ORDER_PAYMENTS WHERE CREATED_DATE BETWEEN 1726138792000 AND 1726138793000) AS filtered_data

Caused by: oracle.jdbc.OracleDatabaseException: ORA-00933: SQL command not properly ended

   String query = String.format("(SELECT * FROM %s WHERE %s) AS filtered_data", tableName, whereClause);


public Dataset<Row> readFromDBWithFilter(long startMillis, long endMillis, String tableName) {
        String query = String.format(
                "SELECT * FROM (SELECT * FROM %s WHERE CREATED_DATE BETWEEN %d AND %d) filtered_data",
                tableName, startMillis, endMillis
                );
        return sparkSession.read()
                .format("jdbc")
                .option("url", jdbcConfig.getJdbcUrl())
                .option("driver", jdbcDriver)
                .option("dbtable", query)
                .option("user", jdbcUserName)
                .option("password", jdbcPassword)
                .load();
    }
