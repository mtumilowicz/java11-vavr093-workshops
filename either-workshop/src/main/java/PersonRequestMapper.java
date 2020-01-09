import io.vavr.control.Either;

class PersonRequestMapper {
    static Either<String, Person> toPerson(PersonRequest request) {
        return Either.right(Person.builder()
                .id(request.getId())
                .age(request.getAge())
                .build());
    }
}
