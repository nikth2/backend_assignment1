package repositories;

import daos.TransactionDAO;
import daos.entities.TransactionEntity;
import model.Transaction;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class TransactionRepositoryImplTest {
    private final TransactionDAO transactionDAO = Mockito.mock(TransactionDAO.class);
    private final TransactionRepository transactionRepository = new TransactionRepositoryImpl(transactionDAO);

    @Test
    public void shouldRetrieveAllTransactions() {
        List<TransactionEntity> mockTransactionEntities = List.of(
                createTransactionEntity(10, "john@test.com", Instant.parse("2020-01-01T00:00:00.00Z"))
        );
        Mockito.stub(transactionDAO.getTransactions()).toReturn(mockTransactionEntities);

        List<Transaction> transactions = transactionRepository.getTransactions();

        assertEquals(1, transactions.size());
        assertEquals("john@test.com", transactions.get(0).getEmailAddress());
        assertEquals(LocalDateTime.of(2020, 1, 1, 2, 0, 0), transactions.get(0).getDate());

    }

    @Test
    public void shouldRetrieveTransactionsAfterDatetime() {
        List<TransactionEntity> mockTransactionEntities = List.of(
                createTransactionEntity(10, "john@test.com", Instant.parse("2020-01-01T00:00:00.00Z")),
                createTransactionEntity(15, "john@test.com", Instant.parse("2020-06-01T00:00:00.00Z")),
                createTransactionEntity(20, "john@test.com", Instant.parse("2020-06-01T02:00:00.00Z"))
        );
        Mockito.stub(transactionDAO.getTransactions()).toReturn(mockTransactionEntities);

        LocalDateTime fromDateTime = LocalDateTime.of(2020, 6, 1, 4, 0);
        List<Transaction> transactions = transactionRepository.getTransactions(fromDateTime);

        assertEquals(1, transactions.size());
        assertEquals(20, transactions.get(0).getAmount());
        assertEquals(LocalDateTime.of(2020, 6, 1, 5, 0, 0), transactions.get(0).getDate());

    }

    private TransactionEntity createTransactionEntity(int amount, String email, Instant date) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmount(amount);
        transactionEntity.setEmail(email);
        transactionEntity.setDate(date);
        return transactionEntity;
    }

}