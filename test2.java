import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionOrderService {

    private final ObjectMapper objectMapper;

    public TransactionOrderService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TransactionOrderResponse mapResultToResponse(Object[] result) {
        // Initialize response
        TransactionOrderResponse response = new TransactionOrderResponse();

        // Map transactions
        Object transactionsRaw = result[0];
        List<TransactionDto> transactions = new ArrayList<>();

        if (transactionsRaw instanceof List<?>) {
            // If the first index is a list, iterate through and map each transaction
            List<?> transactionList = (List<?>) transactionsRaw;
            for (Object transactionRaw : transactionList) {
                TransactionDto transaction = objectMapper.convertValue(transactionRaw, TransactionDto.class);
                transactions.add(transaction);
            }
        } else {
            // If the first index is a single transaction, map it directly
            TransactionDto transaction = objectMapper.convertValue(transactionsRaw, TransactionDto.class);
            transactions.add(transaction);
        }

        response.setTransactions(transactions);

        // Map order
        Object orderRaw = result[1];
        if (orderRaw != null) {
            OrderDto order = objectMapper.convertValue(orderRaw, OrderDto.class);
            response.setOrder(order);
        }

        return response;
    }
}
