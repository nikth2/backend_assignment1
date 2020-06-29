package async;

import model.Transaction;
import repositories.TransactionRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class AsyncTransactionService {

    private final TransactionRepository transactionRepository;

    public AsyncTransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Future<List<Transaction>> getTransactions() {
        return CompletableFuture.supplyAsync(transactionRepository::getTransactions);
    }

    public Future<Map<String, List<Transaction>>> getTransactionsPerEmail() {
        return CompletableFuture.supplyAsync(transactionRepository::getTransactions)
                .thenApply(this::groupByEmail);
    }

    private Map<String, List<Transaction>> groupByEmail(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getEmailAddress));
    }


}
