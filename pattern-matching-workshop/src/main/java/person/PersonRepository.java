package person;

import io.vavr.control.Option;

public class PersonRepository {
    public static Option<String> findById(int id) {
        return id == 1
                ? Option.some(Integer.toString(id))
                : Option.none();
    }
}
