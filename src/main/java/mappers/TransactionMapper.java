package mappers;

import model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionMapper {
    public Map<String, Double> getAverageAmountPerEmailAddress(List<Transaction> transactions) {

        Map<String, List<Transaction>> transactionPerEmail = new HashMap<>();
        for (Transaction transaction : transactions) {
            String email = transaction.getEmailAddress();

            List<Transaction> personTransactions;
            if (!transactionPerEmail.containsKey(email)) {
                personTransactions = new ArrayList<>();
                transactionPerEmail.put(email, personTransactions);
            } else {
                personTransactions = transactionPerEmail.get(email);
            }

            personTransactions.add(transaction);
        }

        Map<String, Double> averageTransactionPerEmail = new HashMap<>();
        for (Map.Entry<String, List<Transaction>> entry : transactionPerEmail.entrySet()) {
            List<Transaction> personTransactions = entry.getValue();
            long sum = 0;
            for (Transaction personTransaction : personTransactions) {
                sum += personTransaction.getAmount();
            }
            averageTransactionPerEmail.put(entry.getKey(), ((double) sum / personTransactions.size()));
        }

        return averageTransactionPerEmail;
    }

}
