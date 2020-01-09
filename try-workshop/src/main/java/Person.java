import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.function.Predicate;

@Value
@Wither
@Builder
class Person {
    int id;
    int age;

    static Predicate<Person> isAdult() {
        return person -> person.age >= 18;
    }
}

class NotAnAdultException extends Exception {

}
