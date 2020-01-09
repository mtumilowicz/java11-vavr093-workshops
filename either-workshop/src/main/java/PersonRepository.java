import io.vavr.control.Either;

class PersonRepository {
    static Either<String, Person> findById(int id) {
        switch (id) {
            case 1:
                return Either.right(Person.builder().id(1).build());
            case 2:
                return Either.right(Person.builder().id(2).build());
            default:
                return Either.left("cannot find person with id = " + id);
        }
    }
    
    static Either<String, Person> save(Person person) {
        return Either.right(person);
    }
}
