package mappers;

import model.Person;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AgeMapperTest {

    private final AgeMapper ageMapper = new AgeMapper();

    @Test
    public void shouldGetAverageMaleAge() {

        List<Person> persons = Arrays.asList(
                createPerson("John", 30, Person.Sex.MALE),
                createPerson("Bob", 40, Person.Sex.MALE),
                createPerson("Jane", 25, Person.Sex.FEMALE)
        );

        double average = ageMapper.getAverageMaleAge(persons);

        assertEquals(35, average, 0);
    }

    private Person createPerson(String name, int age, Person.Sex gender) {
        Person person = new Person();
        person.setName(name);
        person.setAge(age);
        person.setGender(gender);
        return person;
    }
}