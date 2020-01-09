import io.vavr.control.Try;

class PersonRepository {
    static Try<Person> findById(int id) {
        switch (id) {
            case 1:
                return Try.of(() -> Person.builder().id(1).build());
            case 2:
                return Try.of(() -> Person.builder().id(2).build());
            case 3:
                return Try.of(() -> Person.builder().id(3).build());
            default:
                return Try.failure(new EntityNotFoundException());
        }
    }
    
    static void save(Person person) {
        switch (person.getId()) {
            case 1:
                return;
            case 2:
                throw new PersonModifiedInMeantimeException();
            default:
                throw new DatabaseConnectionProblem(person.getId());
        }
    }

    static Person saveFunctional(Person person) {
        return null; // rewrite save in a more functional way
    }
}

class PersonModifiedInMeantimeException extends RuntimeException {

}

class EntityNotFoundException extends RuntimeException {

}