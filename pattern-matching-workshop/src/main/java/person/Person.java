package person;

import io.vavr.API;
import lombok.Builder;
import lombok.Value;

import java.util.function.Predicate;

import static io.vavr.API.*;

@Value
@Builder
public class Person {
    PersonType type;
    boolean active;
    Account account;
    Address address;
    
    public static Predicate<Person> hasType(PersonType type) {
        return p -> Match(p.type).of(
                Case(API.$(type), true),
                Case($(), false)
        );
    }

    public boolean hasBigSalary() {
        return getSalary().getValue() > 1000;
    }

    public Salary getSalary() {
        return account.getSalary();
    }
}
