package workshops;

import bill.Invoice;
import bill.PaymentType;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;
import credit.CreditAssessmentService;
import io.vavr.CheckedRunnable;
import io.vavr.collection.HashSet;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.NonNull;
import out.Display;
import person.*;
import person.request.PersonRequest;
import person.request.ValidPersonRequest;
import tax.TaxService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Predicate;

import static io.vavr.Predicates.is;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;

public class Workshop {

    /**
     * very simple number converter
     * <p>
     * we want to show, that every classic
     * switch-case construction could be rewritten using pattern matching
     * and be more concise, clean and easy to read
     */
    public static int numberConverter(String number) {
        Preconditions.checkState(nonNull(number), "value not supported");

        switch (number) { // hint: Match(number).of
            case "one": // hint: Case($("one"), 1)
                return 1;
            case "two":
                return 2;
            case "three":
                return 3;
            default:
                throw new IllegalStateException("value not supported");
        }
    }

    /**
     * very simple threshold based logic
     * <p>
     * we want to show that every well-known
     * multiple ifs construction could be rewritten using pattern matching
     * and be more concise, clean and easy to read
     */
    public static String thresholds(int input) {
        Preconditions.checkArgument(input >= 0, "only positive numbers!");

        Range<Integer> threshold1 = Range.closed(0, 50);
        Range<Integer> threshold2 = Range.closed(51, 150);
        Range<Integer> threshold3 = Range.atLeast(151);

        // hint: Match(input).of
        if (threshold1.contains(input)) { // hint: Case($(threshold1::contains), "threshold1")
            return "threshold1";
        }
        if (threshold2.contains(input)) {
            return "threshold2";
        }
        if (threshold3.contains(input)) {
            return "threshold3";
        }

        return "threshold3";
    }

    /**
     * very simple enum based logic
     * <p>
     * we want to show that every well-known enum switch-case construction
     * could be rewritten using pattern matching and be more concise, clean and easy to read
     * <p>
     * this method simply loads appropriate stats for a given enum in a way:
     * VIP -> full stats (vips are not very common, and full stats are possibly time consuming
     * so we may want to load it only for special client to show for example highly dedicated
     * marketing suggestions)
     * REGULAR -> we load ordinary, highly optimized stats - compromise between time-consumption
     * and their scope
     * TEMPORARY -> it's just a temporary client (for example - performs buy without permanent account),
     * so we don't want to gather all info about him, or even we do not have possibility to do it
     */
    public static String switchOnEnum(@NonNull Person person) {
        Preconditions.checkState(nonNull(person.getType()), "value not supported");

        switch (person.getType()) { // hint: Match(person.getType()).of
            case VIP: // hint: Case($(PersonType.VIP), () -> StatsService.getFullStats(person))
                return StatsService.getFullStats(person);
            case REGULAR:
                return StatsService.getStats(person);
            case TEMPORARY:
                return StatsService.getFastStats(person);
            default:
                throw new IllegalStateException("value not supported");
        }
    }

    /**
     * nearly every day we met conversions: X -> Y with a non-null guard
     * <p>
     * it is worth mentioning that such converter could be rewritten
     * using pattern-matching in some cases with a gain in a readability
     * <p>
     * this method simply converts String date -> LocalDate
     * and if date is null - it returns null
     */
    public static LocalDate rawDateMapper(String date) {
        return Objects.isNull(date) // hint: Match(date).of
                ? null // hint: Case($(isNull()), () -> null)
                : LocalDate.parse(date);
    }

    /**
     * nearly every day we met conversions: X -> Option<Y>
     * it is worth mentioning that such conversion could be rewritten
     * using pattern-matching in some cases with a gain in a readability
     * <p>
     * this method simply converts String date -> Option<LocalDate>
     * and if date is null - it returns Option.none()
     */
    public static Option<LocalDate> optionDateMapper(String date) {
        return Option.of(date) // hint: Match(date).option
                .map(LocalDate::parse); // hint: Case($(isNotNull()), LocalDate::parse))
    }

    /**
     * every use of either's fold method could be rewritten using pattern matching
     * in some cases - it is easier to read
     * <p>
     * the purpose of this example is to show, that we could think about pattern matching
     * as a way of object's decomposition
     * switch(either)
     * case Left - do something
     * case Right - do something else
     */
    public static Either<String, Person> eitherDecompose(Either<PersonRequest, ValidPersonRequest> either) {
        // Match(either).of
        return Objects.isNull(either)
                ? Either.left("cannot be null")
                // hint: Case($Left($()), PersonService::patch)
                : either.fold(PersonService::patch, PersonService::assemblePerson);
    }

    /**
     * every final consumption of Option could be rewritten using pattern matching
     * in some cases - it is easier to read, especially when it comes to complex side-effects
     * <p>
     * the purpose of this example is to show that we could think about pattern matching
     * as a way of object's decomposition
     * switch(option)
     * case None - run some action (side-effects)
     * case Some - run other action (side-effects)
     */
    public static void optionDecompose(int id, Display display) {
        PersonRepository.findById(id) // Match(PersonRepository.findById(id)).of
                // Case($None(), run(() -> display.push("cannot find person with id = " + id)))
                .onEmpty(() -> display.push("cannot find person with id = " + id))
                .forEach(value -> display.push("person: " + value + " processed"));
    }

    /**
     * every final consumption of Try could be rewritten using pattern matching
     * in some cases - it is easier to read, especially when it comes to complex side-effects
     * <p>
     * the purpose of this example is to show, that we could think about pattern matching
     * as a way of object's decomposition
     * switch(try)
     * case Success - run some action (side-effects)
     * case Failure - run other action (side-effects)
     */
    public static void tryDecompose(String number, Display display) {
        Try.of(() -> Integer.parseInt(number)) // Match(...).of
                // Case($Success($()), i -> run(() -> display.push("squared number is: " + i * i)))
                .onSuccess(i -> display.push("squared number is: " + i * i))
                .onFailure(ex -> display.push("cannot square number: " + ex.getLocalizedMessage()));
    }

    /**
     * every ternary operator could be rewritten using pattern matching
     * in some cases - it is easier to read
     * <p>
     * the purpose of this example is to show, that we could think about pattern matching
     * as a generalization of if-statement or ternary-statement
     */
    public static String ifSyntax(Person person) {
        // Match(person).of
        if (Objects.isNull(person)) {
            return "cannot be null";
        }

        return person.isActive() // Case($(Person::isActive), PersonService::disable)
                ? PersonService.disable(person)
                : PersonService.activate(person);
    }

    /**
     * every logic based on dates could be represented using pattern matching
     * nearly always - in a more concise, cleaner and easier to read way
     * <p>
     * the purpose of this example is to show that we could think about pattern matching
     * as a way of decomposition of LocalDate based on some predicates:
     * switch(localDate)
     * case (predicate1(localDate)) do something
     * case (predicate2(localDate)) do something else
     */
    public static int getTaxRateFor(@NonNull LocalDate date) {
        // Match(date).of
        Predicate<Integer> before2010 = year -> year < 2010;
        Predicate<Integer> after2010 = year -> year > 2010;
        Predicate<Integer> before2015 = year -> year < 2015;

        // implement java/workshops/DecompositionWorkshop.LocalDate and rebuild project
        // Case($LocalDate($(before2010.or(is(2010))), $(), $()), TaxService::taxBeforeOr2010)
        if (before2010.or(is(2010)).test(date.getYear())) {
            return TaxService.taxBeforeOr2010();
        }

        if (after2010.and(before2015).test(date.getYear())) {
            return TaxService.taxFrom2010To2015();
        }

        // (default) - it has to be >= 2015 after bouncing from above ifs
        return TaxService.taxAfterOr2015();
    }

    /**
     * we often need to extract some objects from other object and build
     * another object from them (based on some additional logic) - pattern
     * matching is a perfect for such cases
     * <p>
     * suppose that we have a person, and we want to assess credit risk
     * based on account status and address
     * without pattern matching we often have to perform it in a completely structural
     * way, firstly declaring all needed objects:
     * Address address = person.getAddress();
     * Account account = person.getAccount();
     * ...
     * and then create what we really need
     * RiskSubjects.city(address.city)
     * .country(address.country)
     * .quarter(address.quarter)
     * .balance(account.balance)
     * however - we could extract that objects and then construct the other
     * in a more functional way:
     * Case(decompose person on account and address, (account, address) -> ...
     * <p>
     * note that maybe on that example the gain of using pattern matching is not easily
     * visible, but suppose that we have to construct two types of objects:
     * VipRiskSubject and RegularRiskSubject basing on account balance - ifs
     * could be converted to nested pattern matching - quite similar to getTaxRateFor example
     */
    public static Integer personDecompose(@NonNull Person person) {
        // Match(person).of
        // go to DecompositionWorkshop.PersonByCreditAssessSubjects, implement method and rebuild project
        // Case($PersonByCreditAssessSubjects($(), $()), (account, address) -> ...
        Account account = person.getAccount();
        return CreditAssessmentService.serviceMethodAssess(CreditAssessSubjects.builder()
                .balance(account.getBalance())
                .salary(account.getSalary())
                .country(person.getAddress().getCountry())
                .build()
        );
    }

    /**
     * we often meet logic steered by set (or iterable) membership
     * it is usually pile of if-then-return statements
     *  if set1.contains(x) return y1
     *  if set2.contains(x) return y2
     *  if set3.contains(x) return y3
     *  otherwise throw exception
     * which is quite noisy and vague
     * 
     * the goal of this example is to show how to rewrite it using pattern matching
     * 
     * this method simply returns type (for example for front-end) based on chosen payment
     *  CREDIT_CARD, GIFT_CARD -> "card"
     *  CASH -> "cash"
     *  PAYPAL -> "online"
     *  BLIK, APPLE_PAY -> "mobile"
     *  null -> "not paid yet"
     */
    public static String isInTest(@NonNull Invoice invoice) {
        var paymentType = invoice.getPaymentType();
        // Match(person).of
        // Case($(isIn(...)), ...),
        if (HashSet.of(PaymentType.CREDIT_CARD, PaymentType.GIFT_CARD).contains(paymentType)) {
            return "card";
        }
        if (HashSet.of(PaymentType.CASH).contains(paymentType)) {
            return "cash";
        }
        if (HashSet.of(PaymentType.PAYPAL).contains(paymentType)) {
            return "online";
        }
        if (HashSet.of(PaymentType.BLIK, PaymentType.APPLE_PAY).contains(paymentType)) {
            return "mobile";
        }
        if (Objects.isNull(paymentType)) {
            return "not paid yet";
        }
        
        throw new IllegalArgumentException("value not supported: " + paymentType);
    }

    /**
     * we often need to perform different actions when different sets of predicates are held
     * <p>
     * suppose that we have a person, and we want to do something like this:
     * if (person.isSomething() and person.isSomethingElse() and person.hasSomething())
     * do some action
     * if (not person.isSomething() and person.hasSomethingElse)
     * do some other action
     * otherwise
     * do default
     * <p>
     * this example shows that it could be easily written using pattern matching
     * with a gain in readability and clarity
     */
    public static String allOfTest(@NonNull Person person) {
        // Match(person).of
        // Case($(allOf(Person.hasType(PersonType.VIP), Person::isActive)), "vip + active")
        if (Person.hasType(PersonType.VIP).and(Person::isActive).test(person)) {
            return "vip + active";
        }
        if (Person.hasType(PersonType.VIP).and(not(Person::isActive)).test(person)) {
            return "vip + not active";
        }
        if (Person.hasType(PersonType.REGULAR).and(Person::isActive).test(person)) {
            return "regular + active";
        }
        if (Person.hasType(PersonType.REGULAR).and(not(Person::isActive)).test(person)) {
            return "regular + not active";
        }
        if (Person.hasType(PersonType.TEMPORARY).and(Person::isActive).test(person)) {
            return "temporary + active";
        }
        if (Person.hasType(PersonType.TEMPORARY).and(not(Person::isActive)).test(person)) {
            return "temporary + not active";
        }
        throw new IllegalArgumentException("condition not supported");
    }

    /**
     * we often need to perform some actions that depends on the class of the objects
     * it leads into many if statements with instanceof
     * <p>
     * if (x instance of X1)
     * do something
     * if (x instance of X2)
     * do something else
     * otherwise
     * do default
     * <p>
     * or quite similar issue: try with multiple catch statements
     * <p>
     * this example shows that situations sketched above could be rewritten using
     * pattern matching with a gain in clarity and readability
     */
    public static String instanceOfTest(@NonNull CheckedRunnable runnable) {
        try {
            runnable.run();
            return "no exception";
        } catch (IllegalArgumentException ex) {
            return "IllegalArgumentException";
        } catch (RuntimeException ex) {
            return "RuntimeException";
        } catch (IOException ex) {
            return "IOException";
        } catch (Throwable ex) {
            // Match(ex).of
            // Case($(instanceOf(IllegalArgumentException.class)), "IllegalArgumentException")
            return "handle rest";
        }
    }

    /**
     * we often need to perform different actions when different sets of predicates
     * are not held
     * <p>
     * suppose that we have a person, and we want to do something like this:
     * if (not person.isSomething() and not person.isSomethingElse() and not person.hasSomething())
     * do some action
     * if (not person.isSomething() and not person.hasSomethingElse())
     * do some other action
     * otherwise
     * do default
     * <p>
     * this example shows that it could be easily written using pattern matching
     * with a gain in readability and clarity
     */
    public static String noneOfTest(@NonNull Person person) {
        // Match(person).of
        // Case($(noneOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle special")
        return not(Person.hasType(PersonType.VIP).or(Person::hasBigSalary)).test(person)
                ? "handle special"
                : "handle rest";
    }

    /**
     * we often need to perform different actions when at least one predicate from appropriate
     * set is held
     * <p>
     * suppose that we have a person, and we want to do something like this:
     * if (person.isSomething() or person.isSomethingElse() or person.hasSomething())
     * do some action
     * if (person.isSomething() or person.hasSomethingElse())
     * do some other action
     * otherwise
     * do default
     * <p>
     * this example shows that it could be easily written using pattern matching
     * with a gain in readability and clarity
     */
    public static String anyOfTest(@NonNull Person person) {
        // Match(person).of
        // Case($(anyOf(Person.hasType(PersonType.VIP), Person::hasBigSalary)), "handle special")
        return Person.hasType(PersonType.VIP).or(Person::hasBigSalary).test(person)
                ? "handle special"
                : "handle rest";
    }
}
