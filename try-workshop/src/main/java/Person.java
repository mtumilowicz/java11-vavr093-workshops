import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.function.Predicate;

/**
 * Created by mtumilowicz on 2019-03-03.
 */
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
