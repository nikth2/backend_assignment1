package mappers;

import model.Transaction;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TransactionMapperTest {
    private final TransactionMapper transactionMapper = new TransactionMapper();

    @Test
    public void shouldGetAverageTransactionAmountPerEmail() {
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(createTransaction(10, "john@test.com"));
        transactions.add(createTransaction(20, "john@test.com"));
        transactions.add(createTransaction(10, "bob@test.com"));
        transactions.add(createTransaction(5, "bob@test.com"));
        transactions.add(createTransaction(100, "jane@test.com"));

        Map<String, Double> average = transactionMapper.getAverageAmountPerEmailAddress(transactions);
        assertEquals(15, average.get("john@test.com"), 0);
        assertEquals(7.5, average.get("bob@test.com"), 0);
        assertEquals(100, average.get("jane@test.com"), 0);
        assertEquals(3, average.size());
    }

    private Transaction createTransaction(int amount, String emailAddress) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setEmailAddress(emailAddress);
        return transaction;
    }

}