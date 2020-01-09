import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.function.IntPredicate;
import java.util.function.Predicate;

@Value
@Wither
@Builder
class Person {
    int id;
    int age;
    boolean active;
    
    Person activate() {
        return this.withActive(true);
    }
}

@Value(staticConstructor = "of")
class PersonStats {
    Person person;
    int stats;
    
    static Predicate<PersonStats> matches(IntPredicate predicate) {
        return ps -> predicate.test(ps.stats);
    }
}
