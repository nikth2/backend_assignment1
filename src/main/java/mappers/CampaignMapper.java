package mappers;

import model.Person;
import model.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class CampaignMapper {
    public List<Person> getWeekIdleAccounts(List<Person> persons, List<Transaction> transactions) {

        return persons.stream()
                .filter(person -> person.getAge() > 18)
                .filter(person -> {
                    String email = person.getEmailAddress();
                    return transactions.stream()
                            .filter(transaction -> transaction.getEmailAddress().equals(email))
                            .noneMatch(this::isInLastSevenDays);
                })
                .collect(Collectors.toList());

    }

    private boolean isInLastSevenDays(Transaction transaction) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        return transaction.getDate().isAfter(sevenDaysAgo);
    }
}
