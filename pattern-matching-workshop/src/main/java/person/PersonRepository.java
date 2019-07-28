package person;

import io.vavr.control.Option;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
public class PersonRepository {
    public static Option<String> findById(int id) {
        return id == 1
                ? Option.some(Integer.toString(id))
                : Option.none();
    }
}
