INSERT INTO RECON_FILE_SUMMARY (
    RFS_ID,
    CONFIG_ID,
    SFTP_PATH,
    S3_PATH,
    FILE_NAME,
    BANK_ID,
    FILE_RECEIVED_TIME,
    FILE_UPLOAD_TIME,
    TOTAL_RECORDS,
    TOTAL_AMOUNT,
    PARSING_STATUS,
    MATCHED_RECORDS,
    UNMATCHED_RECORDS,
    DUPLICATE_RECORDS,
    RECON_STATUS,
    RECON_TIME,
    SETTLEMENT_STATUS,
    SETTLEMENT_TIME,
    REMARK
)
VALUES (
    HEXTORAW('1234567890ABCDEF1234567890ABCDEF'), -- RFS_ID as UUID (16-byte hex)
    HEXTORAW('FEDCBA0987654321FEDCBA0987654321'), -- CONFIG_ID as UUID (16-byte hex)
    '/path/to/sftp/file.csv',
    's3://bucket-name/path/to/file.csv',
    'file.csv',
    101, -- BANK_ID
    TO_TIMESTAMP('2024-05-30 10:30:00', 'YYYY-MM-DD HH24:MI:SS'),
    TO_TIMESTAMP('2024-05-30 10:45:00', 'YYYY-MM-DD HH24:MI:SS'),
    1000, -- TOTAL_RECORDS
    500000.75, -- TOTAL_AMOUNT
    'SUCCESS',
    '950',
    '45',
    '5',
    'RECONCILED',
    TO_TIMESTAMP('2024-05-30 11:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    'SETTLED',
    TO_TIMESTAMP('2024-05-30 12:00:00', 'YYYY-MM-DD HH24:MI:SS'),
    'All records processed successfully.'
);
