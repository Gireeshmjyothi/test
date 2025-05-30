CREATE TABLE RECON_FILE_SUMMARY
   (
        RFS_ID                      RAW(16) PRIMARY KEY,
        CONFIG_ID                   RAW(16),
        SFTP_PATH                   VARCHAR2(500),
        S3_PATH                     VARCHAR2(500),
        FILE_NAME                   VARCHAR2(50),
        BANK_ID                     NUMBER,
        FILE_RECEIVED_TIME          TIMESTAMP,
        FILE_UPLOAD_TIME            TIMESTAMP,
        TOTAL_RECORDS               NUMBER,
        TOTAL_AMOUNT                NUMBER(20,2),
        PARSING_STATUS              VARCHAR2(50),
        MATCHED_RECORDS             VARCHAR2(50),
        UNMATCHED_RECORDS           VARCHAR2(50),
        DUPLICATE_RECORDS           VARCHAR2(50),
        RECON_STATUS                VARCHAR2(50),
        RECON_TIME                  TIMESTAMP,
        SETTLEMENT_STATUS           VARCHAR2(50),
        SETTLEMENT_TIME             TIMESTAMP,
        REMARK                      VARCHAR2(500)
   );
