import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public List<MerchantOrderPaymentDto> readCSVByJava(String filePath) {
    List<MerchantOrderPaymentDto> records = new ArrayList<>();

    try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
        String[] fields;
        reader.readNext(); // Skip header

        while ((fields = reader.readNext()) != null) {
            if (fields.length >= 5) {
                MerchantOrderPaymentDto mop = new MerchantOrderPaymentDto();
                mop.setMId(fields[0].trim());
                mop.setOrderRefNumber(fields[1].trim());
                mop.setSbiOrderRefNumber(fields[2].trim());
                mop.setAtrnNumber(fields[3].trim());

                String debitStr = fields[4].trim();
                mop.setDebitAmount(debitStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(debitStr));

                records.add(mop);
            }
        }
    } catch (IOException | CsvValidationException e) {
        log.error("Error reading CSV: {}", e.getMessage(), e);
    }

    return records;
}
