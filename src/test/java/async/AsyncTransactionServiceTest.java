package async;

import model.Transaction;
import org.junit.Test;
import repositories.TransactionRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AsyncTransactionServiceTest {

    private TransactionRepository transactionRepository = mock(TransactionRepository.class);
    private final AsyncTransactionService transactionService = new AsyncTransactionService(transactionRepository);

    @Test
    public void shouldGetTransactionsAsychronously() throws ExecutionException, InterruptedException {

        stub(transactionRepository.getTransactions())
                .toReturn(Collections.emptyList());

        Future<List<Transaction>> future = transactionService.getTransactions();

        assertEquals(Collections.emptyList(), future.get());
    }

    @Test
    public void shouldGetTransactionsByEmailAsychronously() throws ExecutionException, InterruptedException {
        Transaction dummyTransaction = new Transaction();
        dummyTransaction.setEmailAddress("john@test.com");
        dummyTransaction.setAmount(10);

        stub(transactionRepository.getTransactions())
                .toReturn(Collections.singletonList(dummyTransaction));

        Future<Map<String, List<Transaction>>> future = transactionService.getTransactionsPerEmail();

        Map<String, List<Transaction>> expectedGrouping = future.get();

        assertEquals(1, expectedGrouping.size());
        assertEquals(Collections.singletonList(dummyTransaction), expectedGrouping.get("john@test.com"));
    }
}