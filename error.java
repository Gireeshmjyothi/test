16:58:07.656 [http-nio-8080-exec-1] INFO  com.rajput.service.SparkService - Error while connect to SQL : [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column or function parameter with name `sbiOrderRefNumber` cannot be resolved. Did you mean one of the following? [`ATRN_NUM`, `ORDER_AMOUNT`, `CHANNEL_BANK`, `CIN`, `FAIL_REASON`]. 
16:58:07.659 [http-nio-8080-exec-1] ERROR org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet] - Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: java.lang.RuntimeException: [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column or function parameter with name `sbiOrderRefNumber` cannot be resolved. Did you mean one of the following? [`ATRN_NUM`, `ORDER_AMOUNT`, `CHANNEL_BANK`, `CIN`, `FAIL_REASON`].] with root cause
java.lang.RuntimeException: [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column or function parameter with name `sbiOrderRefNumber` cannot be resolved. Did you mean one of the following? [`ATRN_NUM`, `ORDER_AMOUNT`, `CHANNEL_BANK`, `CIN`, `FAIL_REASON`].
	at com.rajput.service.SparkService.getDbDataSet(SparkService.java:97) ~[main/:?]
	at com.rajput.service.SparkService.process(SparkService.java:29) ~[main/:?]
	at com.rajput.controller.SparkController.home(SparkController.java:19) ~[main/:?]


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MerchantOrderPayment implements Serializable {
    @JsonProperty("MERCHANT_ID")
    private String mid;
    @JsonProperty("ORDER_REF_NUMBER")
    private String orderRefNumber;
    @JsonProperty("SBI_ORDER_REF_NUMBER")
    private String sbiOrderRefNumber;
    @JsonProperty("ATRN_NUMBER")
    private String atrnNum;
    @JsonProperty("DEBIT_AMT")
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

    Dataset<Row> datasetRow = sparkSession.read().option("header", true).option("inferSchema", true)
                   .jdbc(url, table, connectionProperties);

           /*Dataset<Row> rowDataset = sparkSession.read()
                   .format("jdbc")
                   .option("url", url)
                   .option("user", user)
                   .option("password", password)
                   .option("dbtable", table)
                   .option("driver", "oracle.jdbc.OracleDriver")
                   .load();*/
       /* Dataset<Row> datasetRow = sparkSession.read().option("header", true).option("inferSchema", true)
                .csv("data/employeeDb.csv").cache();*/
           datasetRow.withColumnRenamed("MERCHANT_ID", "mid");
           datasetRow.withColumnRenamed("ORDER_REF_NUMBER", "orderRefNumber");
           datasetRow.withColumnRenamed("SBI_ORDER_REF_NUMBER", "sbiOrderRefNumber");
           datasetRow.withColumnRenamed("ATRN_NUM", "atrnNum");
           datasetRow.withColumnRenamed("DEBIT_AMT", "debitAmt");
           return datasetRow.as(merchantOrderPayment);
       }catch (Exception ex){
           log.info("Error while connect to SQL : {} ", ex.getMessage());
           throw new RuntimeException(ex.getMessage());
       }
    }
