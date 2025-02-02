--liquibase formatted sql
--changeset TRANSACTION:16

 --MERCHANT_ORDER INDX AND CONSTRAINTS
CREATE INDEX ORDER_ORDER_REF_NUMBER_IDX ON MERCHANT_ORDERS (ORDER_REF_NUMBER);
CREATE INDEX ORDER_HASH_IDX ON MERCHANT_ORDERS (ORDER_HASH);
CREATE INDEX ORDER_STATUS_IDX ON MERCHANT_ORDERS (STATUS);
CREATE INDEX ORDER_CREATED_DATE_IDX ON MERCHANT_ORDERS (CREATED_DATE);

ALTER TABLE MERCHANT_ORDERS ADD CONSTRAINT ORDER_CUST_FK FOREIGN KEY (CUSTOMER_ID) REFERENCES MERCHANT_CUSTOMER (CUSTOMER_ID);

--MERCHANT_ORDER_PAYMENTS(TRANSACTION )INDX AND CONSTRAINTS
CREATE INDEX TXN_REF_NUMBER_IDX ON MERCHANT_ORDER_PAYMENTS (ORDER_REF_NUMBER);
CREATE INDEX TXN_CREATED_DATE_IDX ON MERCHANT_ORDER_PAYMENTS (CREATED_DATE);
CREATE INDEX TXN_STATUS_IDX ON MERCHANT_ORDER_PAYMENTS (TRANSACTION_STATUS);

--MERCHANT_CUSTOMER INDX AND CONSTRAINTS
CREATE INDEX CUST_CREATED_DATE_IDX ON MERCHANT_CUSTOMER (CREATED_DATE);
CREATE INDEX CUST_MERCH_ID_IDX ON MERCHANT_CUSTOMER (MERCHANT_ID);



--liquibase formatted sql
--changeset TRANSACTION:13

-- CREATE table MERCHANT_CUSTOMER
 CREATE TABLE MERCHANT_CUSTOMER
   (
    CUSTOMER_ID VARCHAR2(50) DEFAULT 'CUST_' || SUBSTR(SYS_GUID(), 1, 20) PRIMARY KEY,
	MERCHANT_ID VARCHAR2(20) NOT NULL,
	CUSTOMER_NAME VARCHAR2(100 BYTE) NOT NULL,
	EMAIL VARCHAR2(100 BYTE) NOT NULL,
	PHONE_NUMBER VARCHAR2(20 BYTE) NOT NULL,
	GST_IN VARCHAR2(18 BYTE),
	GST_IN_ADDRESS VARCHAR2(500 BYTE),
	ADDRESS1 VARCHAR2(100 BYTE) NOT NULL,
	ADDRESS2 VARCHAR2(100 BYTE),
	CITY VARCHAR2(100 BYTE),
	STATE VARCHAR2(100 BYTE),
	COUNTRY VARCHAR2(100 BYTE) NOT NULL,
	PINCODE VARCHAR2(10 BYTE),
	STATUS VARCHAR2(1 BYTE)  DEFAULT 'A',
	CREATED_DATE_NUM NUMBER NOT NULL ENABLE,
	UPDATED_DATE NUMBER,
	CREATED_BY VARCHAR2(100) NOT NULL,
	UPDATED_BY VARCHAR2(100),
  CREATED_DATE DATE default sysdate,
	CONSTRAINT CUST_UK3 UNIQUE (CUSTOMER_ID,EMAIL,PHONE_NUMBER)
 ) PARTITION BY RANGE (CREATED_DATE) INTERVAL (NUMTOYMINTERVAL(1, 'MONTH')) (PARTITION P_START VALUES LESS THAN (TO_DATE('01-01-2022','DD-MM-YYYY')));


--liquibase formatted sql
--changeset TRANSACTION:14

--create table MERCHANT_ORDERS
CREATE TABLE MERCHANT_ORDERS
  (
    SBI_ORDER_REF_NUMBER RAW(16) DEFAULT SUBSTR(SYS_GUID(), 1, 20) PRIMARY KEY,
    MERCHANT_ID          VARCHAR2(20) NOT NULL,
    CUSTOMER_ID          RAW(16),
    CURRENCY_CODE        VARCHAR2(50) NOT NULL,
    ORDER_AMOUNT         NUMBER(16,2) NOT NULL,
    ORDER_REF_NUMBER     VARCHAR2(50),
    STATUS               VARCHAR2(50) NOT NULL,
    OTHER_DETAILS CLOB,
    EXPIRY NUMBER,
    MULTI_ACCOUNTS CLOB,
    PAYMENT_MODE     VARCHAR2(50),
	MERCHANT_TYPE     VARCHAR2(50),
    ORDER_HASH       VARCHAR2(2000),
	ORDER_RETRY_COUNT       NUMBER,
    RETURN_URL       VARCHAR2(2000),
    CREATED_BY       VARCHAR2(100) NOT NULL,
    UPDATED_BY       VARCHAR2(100) ,
    CREATED_DATE_NUM     NUMBER NOT NULL,
    UPDATED_DATE     NUMBER,
    CREATED_DATE DATE default sysdate
  ) PARTITION BY RANGE (CREATED_DATE) INTERVAL (NUMTOYMINTERVAL(1, 'MONTH')) (PARTITION P_START VALUES LESS THAN (TO_DATE('01-01-2022','DD-MM-YYYY')));

