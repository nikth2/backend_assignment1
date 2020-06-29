import model.Person;
import model.Transaction;
import repositories.TransactionRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService {
    private final PersonsService personsService;
    private final TransactionRepository transactionRepository;

    public TransactionService(PersonsService personsService, TransactionRepository transactionRepository) {
        this.personsService = personsService;
        this.transactionRepository = transactionRepository;
    }

    List<String> getPersonRolesOfAllTransactions() {
        return transactionRepository.getTransactions()
                .stream()
                .filter(Objects::nonNull)
                .map(Transaction::getEmailAddress)
                .map(personsService::getPersonByEmailAddress)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Person::getRoles)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

    }
}
