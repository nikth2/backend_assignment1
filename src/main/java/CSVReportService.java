import model.Person;
import model.Transaction;
import repositories.TransactionRepository;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVReportService {

    private final PersonsService personsService;
    private final TransactionRepository transactionRepository;

    public CSVReportService(PersonsService personsService, TransactionRepository transactionRepository) {
        this.personsService = personsService;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieve the average consumption (transaction amount) per @{@link model.Person}'s distinct roles during the last month
     *
     * Note that roles are just tags that each person is assigned. ie 'student', 'gamer', 'athlete', 'parent'
     * a Person may have multiple roles or none.
     *
     * @return data in csv file format,
     *         where the first line depict the roles
     *         and the second line the average consumption per role
     * ie: (formatted example -- the actual output should be just comma separated)
     * |student, gamer, parent|
     * |10.50  , 20.10, 0     |
     */

    public String getAverageConsumptionPerRoleDuringTheLastMonth() {
        List<Transaction> transactionList = this.transactionRepository
            .getTransactions(LocalDateTime.now().minusMonths(1));
        List<String> roleList = transactionList.stream()
            .map(tr->this.personsService.getPersonByEmailAddress(tr.getEmailAddress()).get().getRoles())
            .flatMap(roles->roles.stream()).distinct().collect(Collectors.toList());

        Map<String, Double> map = roleList.stream().map(role -> new AbstractMap.SimpleEntry<String, Double>(role,
            transactionList.stream().filter(tr -> {
                return this.personsService.getPersonByEmailAddress(tr.getEmailAddress()).get().getRoles().contains(role);
            }).mapToDouble(Transaction::getAmount).average().getAsDouble()
        )).collect(Collectors.toMap(entry-> entry.getKey(), entry->entry.getValue()));

        String header = map.keySet().stream().collect(Collectors.joining(","));
        String body = map.values().stream().map(String::valueOf).collect(Collectors.joining(","));
        return header+"\n"+body;
    }

    /**
     * This method is also functional, with a different approach from the 1st.
     * @return
     */
    public String getAverageConsumptionPerRoleDuringTheLastMonth2() {
        Map<String,Stream> m = new HashMap<>();

        Map<String, List<Transaction>> transactions = this.transactionRepository
            .getTransactions(LocalDateTime.now().minusMonths(1)).stream()
            .collect(Collectors.groupingBy(Transaction::getEmailAddress));

        Map<Person,List<Long>> map = transactions.entrySet().stream().map(entry ->{
            return new AbstractMap.SimpleEntry<Person, List<Long>>(
                this.personsService.getPersonByEmailAddress(entry.getKey()).orElse(new Person()), entry.getValue()
                .stream().map(Transaction::getAmount).collect(Collectors.toList()));
        }).collect(Collectors.toMap(personDoubleSimpleEntry -> personDoubleSimpleEntry.getKey(),
            personDoubleSimpleEntry -> personDoubleSimpleEntry.getValue()
        ));

        Map<String,List<Long>> finalMap = new HashMap<>();
        map.forEach((person,amountList)->{
            person.getRoles().forEach(role->
                finalMap.merge(role, amountList,(a, b)-> {
                    return Stream.of(a,b).flatMap(x->x.stream()).collect(Collectors.toList());
                }));
        });

        StringBuilder stringBuilder = new StringBuilder();
        String header = finalMap.keySet().stream().collect(Collectors.joining(","));
        stringBuilder.append(header);
        stringBuilder.append("\n");
        stringBuilder.append(finalMap.values().stream()
            .map(l->l.stream().mapToDouble(Double::valueOf).average().getAsDouble()).map(String::valueOf)
            .collect(Collectors.joining(",")));
        return stringBuilder.toString();
    }


}
