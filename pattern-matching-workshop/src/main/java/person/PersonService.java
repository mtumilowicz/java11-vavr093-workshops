package person;

import io.vavr.control.Either;
import lombok.NonNull;
import person.request.PersonRequest;
import person.request.ValidPersonRequest;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static io.vavr.Predicates.allOf;
import static java.util.function.Predicate.not;

public class PersonService {

    public static Either<String, Person> patch(@NonNull PersonRequest personRequest) {
        Predicate<PersonRequest> negativeSalary = request -> request.getSalary() < 0;
        return Match(personRequest).of(
                Case($(negativeSalary),
                        () -> Either.right(
                                Person.builder()
                                        .type(personRequest.getType())
                                        .active(false)
                                        .address(Address.builder()
                                                .city(personRequest.getCity())
                                                .country(personRequest.getCountry())
                                                .build())
                                        .account(Account.builder()
                                                .balance(personRequest.getBalance())
                                                .salary(Salary.of(0))
                                                .build())
                                        .build()
                        )),
                Case($(), () -> Either.left("cannot be fixed, too many errors"))
        );
    }

    public static Either<String, Person> assemblePerson(@NonNull ValidPersonRequest request) {
        return Match(request).of(
                Case($(allOf(PersonService::businessRule1, PersonService::vipIsActive)),
                        /*
                         to consideration: maybe Person creation itself should also be protected 
                         against some of that rules
                         */
                        () -> Either.right(Person.builder()
                                .type(request.getType())
                                .active(request.isActive())
                                .address(Address.builder()
                                        .city(request.getCity())
                                        .country(request.getCountry())
                                        .build())
                                .account(Account.builder()
                                        .salary(request.getSalary())
                                        .balance(request.getBalance())
                                        .build())
                                .build())),
                Case($(), () -> Either.left("not all business rules are matched"))
        );
    }

    public static String activate(@NonNull Person person) {
        return "activated";
    }

    public static String disable(@NonNull Person person) {
        return "deactivated";
    }

    private static boolean businessRule1(@NonNull ValidPersonRequest request) {
        return true;
    }

    private static boolean vipIsActive(@NonNull ValidPersonRequest validRequest) {
        Predicate<ValidPersonRequest> isVip = request -> request.getType() == PersonType.VIP;
        Predicate<ValidPersonRequest> isActive = ValidPersonRequest::isActive;
        return Match(validRequest).of(
                Case($(allOf(isVip, not(isActive))), false),
                Case($(), true)
        );
    }
}
