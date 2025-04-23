 [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column or function parameter with name `atrnNum` cannot be resolved. Did you mean one of the following? [`CIN`, `GST_IN`, `ATRN_NUM`, `PAY_MODE`, `DEBIT_AMT`]. 



@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantOrderPayment implements Serializable {
    private String mid;
    private String orderRefNumber;
    @JsonProperty("SBI_ORDER_REF_NUMBER")
    private String sbiOrderRefNumber;
    private String atrnNum;
    private double debitAmt;
}

CREATE TABLE "MERCHANT_ORDER_PAYMENTS" (	
    "MERCHANT_ID" VARCHAR2(20 BYTE),
	"ORDER_REF_NUMBER" VARCHAR2(50 BYTE), 
	"SBI_ORDER_REF_NUMBER" VARCHAR2(50 BYTE), 
	"ATRN_NUM" VARCHAR2(50 BYTE),
	"BANK_REFERENCE_NUMBER" VARCHAR2(255 BYTE), 
	"CURRENCY_CODE" VARCHAR2(50 BYTE),
	"CHANNEL_BANK" VARCHAR2(100 BYTE),
	"PAY_MODE" VARCHAR2(50 BYTE),
	"GTW_MAP_ID" VARCHAR2(20 BYTE), 
	"PAY_PROC_ID" VARCHAR2(20 BYTE))


	 Dataset<Row> rowDataset = sparkSession.read()
                   .format("jdbc")
                   .option("url", url)
                   .option("user", user)
                   .option("password", password)
                   .option("dbtable", table)
                   .option("driver", "oracle.jdbc.OracleDriver")
                   .load();
       /* Dataset<Row> datasetRow = sparkSession.read().option("header", true).option("inferSchema", true)
                .csv("data/employeeDb.csv").cache();*/
           return rowDataset.as(merchantOrderPayment);
       }catch (Exception ex){
           log.info("Error while connect to SQL : {} ", ex.getMessage());
           throw new RuntimeException(ex.getMessage());
       }
