CREATE TABLE RECON_FILE_DTLS
   (
       ID                      RAW(16),
       FILE_SUMMARY_ID         RAW(16),
       ROW_NUMBER              NUMBER,
       RECORD_TYPE             VARCHAR2(100),
       ATRN_NUM                VARCHAR2(100),
       PAYMENT_AMOUNT          NUMBER(15, 2),
       PAYMENT_DATE            NUMBER,
       BANK_REFERENCE_NUMBER   VARCHAR2(100),
       STATUS                  VARCHAR2(50),
       RECON_STATUS            VARCHAR2(50),
       RECON_TIME              TIMESTAMP,
       SETTLEMENT_STATUS       VARCHAR2(50),
       SETTLEMENT_TIME         TIMESTAMP,
       REMARK                  VARCHAR2(500)
   );
