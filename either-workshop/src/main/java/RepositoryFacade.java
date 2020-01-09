import io.vavr.control.Either;

class CacheRepository {
    static Either<String, String> findById(int id) {
        switch (id) {
            case 2:
                return Either.left("user cannot be found in cache, id = " + id);
            case 3:
                return Either.left("user cannot be found in cache, id = " + id);
            default:
                return Either.right("from cache, id = " + id);
        }
    }
}

class DatabaseRepository {
    static Either<String, String> findById(int id) {
        switch (id) {
            case 3:
                return Either.left("user cannot be found in database, id = " + id);
            default:
                return Either.right("from database, id = " + id);
        }
    }

}
