import io.vavr.control.Either;

import java.util.function.Consumer;
import java.util.function.Function;

public class PersonService {
    static Consumer<Person> updateStats = person -> {
    };

    static Function<Person, Either<String, PersonStats>> loadStats = person -> {
        switch (person.getId()) {
            case 1:
                return Either.right(PersonStats.of(person, 10));
            case 2:
                return Either.right(PersonStats.of(person, 20));
            default:
                return Either.left("cannot load stats for person = " + person.getId());
        }
    };

    /*
    if stats > 15 -> activate user and save it, otherwise return "stats <= 15" message
     */
    static Either<String, Person> process(Person person) {
        return null; // loadStats.apply(person)
            // filter, PersonStats.matches
            // getOrElse, "stats <= 15"
            // map, PersonStats::getPerson
            // map, Person.activate
            // flatMap, PersonRepository.save
    }
}
