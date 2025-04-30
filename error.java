import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public List<MerchantOrderPaymentDto> readCSVByJava(String filePath) {
    List<MerchantOrderPaymentDto> records = new ArrayList<>();

    try (Reader reader = new FileReader(filePath);
         CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

        for (CSVRecord csvRecord : csvParser) {
            MerchantOrderPaymentDto mop = new MerchantOrderPaymentDto();
            mop.setMId(csvRecord.get(0));
            mop.setOrderRefNumber(csvRecord.get(1));
            mop.setSbiOrderRefNumber(csvRecord.get(2));
            mop.setAtrnNumber(csvRecord.get(3));

            String amount = csvRecord.get(13);
            mop.setDebitAmount(new BigDecimal(amount.isEmpty() ? "0" : amount));

            records.add(mop);
        }

    } catch (Exception e) {
        log.error("Error reading CSV file", e);  // full stack trace
    }

    return records;
}
