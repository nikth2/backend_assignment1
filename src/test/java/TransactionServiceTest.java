import model.Person;
import model.Transaction;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import repositories.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

public class TransactionServiceTest {

    private final PersonsService personService = Mockito.mock(PersonsService.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    TransactionService transactionService = new TransactionService(personService, transactionRepository);

    @Test
    public void shouldGetPersonRolesForAllTransactions() {
        // given
        Person john = new Person();
        john.setRoles(Set.of("student", "gamer", "athlete"));
        john.setEmailAddress("john@test.com");

        Person jane = new Person();
        jane.setRoles(Set.of("employee", "athlete"));
        jane.setEmailAddress("jane@test.com");

        // and
        Mockito.stub(personService.getPersonByEmailAddress(Matchers.eq("john@test.com"))).toReturn(Optional.of(john));
        Mockito.stub(personService.getPersonByEmailAddress(Matchers.eq("jane@test.com"))).toReturn(Optional.of(jane));

        // and
        Mockito.stub(transactionRepository.getTransactions())
                .toReturn(
                        List.of(createTransaction(10, "john@test.com"),
                                createTransaction(10, "jane@test.com"),
                                createTransaction(20, "jane@test.com")
                        )
                );

        // when
        List<String> roles = transactionService.getPersonRolesOfAllTransactions();

        assertEquals(4, roles.size());
        assertTrue(roles.containsAll(List.of("student", "gamer", "athlete", "employee")));
    }

    private Transaction createTransaction(int amount, String emailAddress) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setEmailAddress(emailAddress);
        return transaction;
    }

}