import io.vavr.control.Option;

/**
 * Created by mtumilowicz on 2018-11-26.
 */
class FacadeRepository {

    /*
    implement function that will:
        1. return result from cache (CacheRepository)
        1. if not found: return result from database (DatabaseRepository)
        1. if not found: Option.none()
     */
    static Option<String> findById(int id) {
        return Option.none();
    }
}

class CacheRepository {
    static Option<String> findById(int id) {
        return Option.when(id == 1, "from cache");
    }
}

class DatabaseRepository {
    static Option<String> findById(int id) {
        return Option.when(id == 2, "from database");
    }
}
