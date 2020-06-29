package mappers;

import model.Person;

import java.util.List;

public class AgeMapper {
    double getAverageMaleAge(List<Person> persons) {
        long sum = 0;
        long count = 0;
        for (Person p : persons) {
            if (p.getGender() == Person.Sex.MALE) {
                int age = p.getAge();
                sum += age;
                count++;
            }
        }
        return count > 0 ? ((double) sum / count) : 0;
    }
}
