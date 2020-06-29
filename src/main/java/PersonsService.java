import model.Person;

import java.util.Optional;

public interface PersonsService {
    Optional<Person> getPersonByEmailAddress(String email);

}
