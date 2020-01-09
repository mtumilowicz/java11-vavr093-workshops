import io.vavr.control.Try;
import lombok.Value;

class RepositoryFacade {
    /*
    implement function that will try to:
        1. return result from database (DatabaseRepository)
        1. if not found: return result from backup (BackupRepository)
     */
    static Try<String> findById(int id) {
        return null;
    }
}

class CacheRepository {
    static Try<String> findById(int id) {
        if (id == 2) {
            return Try.failure(new CacheUserCannotBeFound(id));
        }
        if (id == 3) {
            return Try.failure(new CacheUserCannotBeFound(id));
        }        
        if (id == 4) {
            return Try.failure(new CacheUserCannotBeFound(id));
        }
        if (id == 5) {
            return Try.failure(new CacheSynchronization());
        }
        return Try.of(() -> "from cache");
    }
}

class DatabaseRepository {
    static Try<String> findById(int id) {
        switch (id) {
            case 2:
                return Try.failure(new DatabaseConnectionProblem(id));
            case 3:
                return Try.failure(new DatabaseUserCannotBeFound());
            default:
                return Try.of(() -> "from database");
        }
    }

}

class BackupRepository {
    static Try<String> findById(int id) {
                return Try.of(() -> "from backup");
    }

}

class DatabaseUserCannotBeFound extends RuntimeException {

}

@Value
class CacheUserCannotBeFound extends RuntimeException {
    int userId;
}

@Value
class DatabaseConnectionProblem extends RuntimeException {
    int userId;
}

class CacheSynchronization extends RuntimeException {
    
}
