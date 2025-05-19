CREATE TABLE YOUR_TABLE_NAME (
    ID                      RAW(16), -- UUID
    FILE_SUMMARY_ID         RAW(16), -- UUID
    ROW_NUMBER              NUMBER,
    RECORD_TYPE             VARCHAR2(100),
    ATRN_NUM                VARCHAR2(100),
    PAYMENT_AMOUNT          NUMBER(15, 2),
    PAYMENT_DATE            DATE,
    BANK_REFERENCE_NUMBER   VARCHAR2(100),
    STATUS                  VARCHAR2(50),
    RECON_STATUS            VARCHAR2(50),
    RECON_TIME              TIMESTAMP,
    SETTLEMENT_STATUS       VARCHAR2(50),
    SETTLEMENT_TIME         TIMESTAMP,
    REMARK                  VARCHAR2(500)
);
