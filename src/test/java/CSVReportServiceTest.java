import model.Person;
import model.Transaction;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import repositories.TransactionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

public class CSVReportServiceTest {

    private final PersonsService personService = Mockito.mock(PersonsService.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
    CSVReportService csvReportService = new CSVReportService(personService, transactionRepository);

    @Test
    public void shouldGetAverageConsumptionPerRoleDuringTheLastMonth() {
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
        Mockito.stub(transactionRepository.getTransactions(any()))
            .toReturn(
                List.of(createTransaction(15, "john@test.com", LocalDateTime.ofInstant(Instant.parse("2020-06-10T00:00:00.00Z"), ZoneId.of("Europe/Athens"))),
                    createTransaction(10, "jane@test.com", LocalDateTime.ofInstant(Instant.parse("2020-06-10T00:00:00.00Z"), ZoneId.of("Europe/Athens"))),
                    createTransaction(50, "jane@test.com", LocalDateTime.ofInstant(Instant.parse("2020-06-10T00:00:00.00Z"), ZoneId.of("Europe/Athens")))//,
                    //createTransaction(5000, "jane@test.com", LocalDateTime.ofInstant(Instant.parse("2020-01-10T00:00:00.00Z"), ZoneId.of("Europe/Athens")))
                )
            );
        // when
        String csvReport = csvReportService.getAverageConsumptionPerRoleDuringTheLastMonth2();
        System.out.println(csvReport);
        Map<String, Double> rolesAverageConsumption = new HashMap<>();
        List<String> csvRoles = Arrays.asList(csvReport.split("\n")[0].split(","));
        List<String> csvValues = Arrays.asList(csvReport.split("\n")[1].split(","));
        for (int i = 0; i < csvRoles.size(); i++) {
            rolesAverageConsumption.put(csvRoles.get(i), Double.parseDouble(csvValues.get(i)));
        }
        assertTrue(rolesAverageConsumption.containsKey("student") && rolesAverageConsumption.get("student") == 15.0);
        assertTrue(rolesAverageConsumption.containsKey("gamer") && rolesAverageConsumption.get("gamer") == 15.0);
        assertEquals(25.00, rolesAverageConsumption.get("athlete").doubleValue(),0.01);
        assertTrue(rolesAverageConsumption.containsKey("employee") && rolesAverageConsumption.get("employee") == 30.0);
    }

    private Transaction createTransaction(int amount, String emailAddress, LocalDateTime date) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setEmailAddress(emailAddress);
        transaction.setDate(date);
        return transaction;
    }
}
