import io.vavr.control.Either;

/**
 * Created by mtumilowicz on 2019-04-26.
 */
class PersonRequestMapper {
    static Either<String, Person> toPerson(PersonRequest request) {
        return Either.right(Person.builder()
                .id(request.getId())
                .age(request.getAge())
                .build());
    }
}
