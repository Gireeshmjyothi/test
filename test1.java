spark-submit --class com.epay.rns.ReconSettlementServiceApplication --master spark://10.30.64.27:7077 --deploy-mode client --jars "C:\jdbcDriver\ojdbc11-23.5.0.24.07.jar" "F:\Epay\epay_recon_settlement_service\build\libs\epay_recon_settlement_service-0.0.1.jar"

spark-submit --class com.epay.rns.ReconSettlementServiceApplication --master spark://10.30.64.27:7077 --deploy-mode client --jars "C:\jdbcDriver\ojdbc11-23.5.0.24.07.jar" "F:\Epay\epay_recon_settlement_service\build\libs\epay_recon_settlement_service-0.0.1.jar"


spark-submit ^
--class com.epay.rns.ReconSettlementServiceApplication ^
--master spark://10.30.64.27:7077 ^
--deploy-mode client ^
--jars "C:\jdbcDriver\ojdbc11-23.5.0.24.07.jar" ^
"F:\Epay\epay_recon_settlement_service\build\libs\epay_recon_settlement_service-0.0.1.jar"
