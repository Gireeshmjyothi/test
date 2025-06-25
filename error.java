Error : 904, Position : 22, SQL = SELECT * FROM (SELECT m FROM MERCHANT_ORDER_PAYMENTS m, RECON_FILE_DTLS r WHERE r.ATRN_NUM = m.ATRN_NUM AND r.RFS_ID = HEXTORAW('1A6CF13CDF224845A15AF740E2716015')) filtered_transaction_data WHERE 1=0, Original SQL = SELECT * FROM (SELECT m FROM MERCHANT_ORDER_PAYMENTS m, RECON_FILE_DTLS r WHERE r.ATRN_NUM = m.ATRN_NUM AND r.RFS_ID = HEXTORAW('1A6CF13CDF224845A15AF740E2716015')) filtered_transaction_data WHERE 1=0, Error Message = ORA-00904: "M": invalid identifier
	
/**
     * This method is used to read data from jdbc by query and filter.
     *
     * @param query table name.
     * @return dataset.
     */
    public Dataset<Row> readFromDBWithFilter(String query) {

        return sparkSession.read()
                .format("jdbc")
                .option("url", jdbcConfig.getJdbcUrl())
                .option("driver", jdbcDriver)
                .option("dbtable", query)
                .option("user", jdbcUserName)
                .option("password", jdbcPassword)
                .load();
    }
