package mappers;

import model.Person;
import model.Transaction;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CampaignMapperTest {

    private final CampaignMapper campaignMapper = new CampaignMapper();

    @Test
    public void shouldGetWeeklyIdlePersons() {
        List<Person> persons = Arrays.asList(
                createPerson("John", "john@test.com", 30),
                createPerson("Bob", "bob@test.com", 40),
                createPerson("Jane", "jane@test.com", 16)
        );

        List<Transaction> transactions = Arrays.asList(
                createTransaction(10, "john@test.com", LocalDateTime.now().minus(1, ChronoUnit.DAYS)),
                createTransaction(15, "john@test.com", LocalDateTime.now().minus(10, ChronoUnit.DAYS)),
                createTransaction(20, "bob@test.com", LocalDateTime.now().minus(30, ChronoUnit.DAYS)),
                createTransaction(30, "jane@test.com", LocalDateTime.now().minus(40, ChronoUnit.DAYS))
        );
        List<Person> accounts = campaignMapper.getWeekIdleAccounts(persons, transactions);

        assertEquals(1, accounts.size());
        assertEquals("bob@test.com", accounts.get(0).getEmailAddress());

    }

    private Person createPerson(String name, String email, int age) {
        Person person = new Person();
        person.setName(name);
        person.setEmailAddress(email);
        person.setAge(age);
        return person;
    }

    private Transaction createTransaction(int amount, String emailAddress, LocalDateTime date) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setEmailAddress(emailAddress);
        transaction.setDate(date);
        return transaction;
    }
}