import io.vavr.Function1
import io.vavr.PartialFunction
import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.collection.Map
import io.vavr.collection.Seq
import io.vavr.control.Option
import io.vavr.control.Try
import spock.lang.Specification

import java.nio.file.NoSuchFileException
import java.time.Month
import java.util.function.*
import java.util.stream.Collectors

/**
 * Created by mtumilowicz on 2019-03-03.
 */
class Workshop extends Specification {

    def "create successful Try with value 1, verify success and value"() {
        given:
        Try<Integer> success = false // create here, hint: success

        expect:
        false // verify success here, hint: isSuccess
        false // verify value here, hint: get
    }

    def "create failed Try with cause IllegalStateException with message: 'wrong status', verify failure, cause and message"() {
        given:
        Try<Integer> fail = false // create here, hint: failure

        expect:
        false // verify failure here, hint: isFailure
        false // verify cause class here, hint: getCause, class
        false // verify cause message here, hint: getCause, getMessage
    }

    def "conversion: option -> try"() {
        given:
        Option<Integer> emptyOption = Option.none()
        Option<Integer> notEmptyOption = Option.some(1)

        when:
        Try<Integer> failTry = emptyOption // convert here, hint: toTry()
        Try<Integer> successTry = notEmptyOption // convert here, hint: toTry()

        then:
        failTry.failure
        failTry.cause.class == NoSuchElementException

        and:
        successTry.success
        successTry.get() == 1
    }

    def "conversion: try -> option"() {
        given:
        Try<Integer> success = Try.of { 1 }
        Try<Integer> fail = Try.failure(new IllegalStateException())

        when:
        Option<Integer> successOption = success // convert here, hint: toOption()
        Option<Integer> failOption = fail // convert here, hint: toOption()

        then:
        successOption == Option.some(1)
        failOption == Option.none()
    }

    def "wrap div (4 / 2) with try"() {
        given:
        BinaryOperator<Integer> div = { a, b -> a / b }

        when:
        Try<Integer> dived = Try.of { -1 } // wrap here

        then:
        dived.success
        dived.get() == 2
    }

    def "wrap div (4 / 0) with try"() {
        given:
        BinaryOperator<Integer> div = { a, b -> a / b }

        when:
        Try<Integer> fail = Try.of { -1 } // wrap here

        then:
        fail.failure
        fail.cause.class == ArithmeticException
    }

    def "wrap parseInt with try, and invoke it on 1 and 'a'"() {
        given:
        Function<String, Integer> parseInt = { Integer.parseInt(it) }

        when:
        Try<Integer> parsed = Try.of { -1 } // wrap here, hint: parseInt.apply('1')
        Try<Integer> notParsed = Try.of { -1 } // wrap here, hint: parseInt.apply('a')

        then:
        parsed.success
        parsed.get() == 1
        notParsed.failure
        notParsed.cause.class == NumberFormatException
    }

    def "checked exceptions handling: wrap method that throws checked exception"() {
        given:
        Try<Integer> one = Parser.parse('1') // go to Parser.parse and implement the method
        Try<Integer> fail = Parser.parse('a') // go to Parser.parse and implement the method

        expect:
        one.success
        one.get() == 1
        fail.failure
        fail.cause.class == CannotParseInteger
    }

    def "sum values of try sequence or return the first failure"() {
        given:
        Function<String, Integer> parse = { Integer.parseInt(it) }
        Try<Integer> parsed1 = Try.of { parse.apply('1') }
        Try<Integer> parsed2 = Try.of { parse.apply('2') }
        Try<Integer> parsed3 = Try.of { parse.apply('3') }
        Try<Integer> parsed4 = Try.of { parse.apply('4') }
        Try<Integer> failure1 = Try.of { parse.apply('a') }
        Try<Integer> failure2 = Try.of { parse.apply('b') }

        and:
        List<Try<Integer>> from1To4 = List.of(parsed1, parsed2, parsed3, parsed4)
        List<Try<Integer>> all = List.of(parsed1, parsed2, parsed3, parsed4, failure1, failure2)

        when:
        Try<Number> sum = from1To4 // sum here from1To4, hint: sequence, map, sum
        Try<Number> fail = all // sum here all, hint: sequence, map, sum

        then:
        sum.success
        sum.get() == 10
        and:
        fail.failure
        fail.cause.class == NumberFormatException
        fail.cause.message == 'For input string: "a"'
    }

    def "count average expenses in a year (by month) or return first failure"() {
        given:
        def spendingByMonth = {
            it.getValue()
        }

        and:
        def spendingByMonthExceptional = {
            switch (it) {
                case Month.MARCH: throw new RuntimeException('Expenses in March cannot be loaded.')
                default: it.getValue()
            }
        }

        and:
        Function<Function<Month, Integer>, Map<Month, Try<Integer>>> expensesByMonthMap = {
            spendingIn ->
                HashMap.ofAll(Arrays.stream(Month.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                { month -> Try.of { spendingIn(month) } })))
        }

        when:
        Seq<Try<Integer>> withoutFailure = expensesByMonthMap.apply(spendingByMonth).values()
        Seq<Try<Integer>> withFailure = expensesByMonthMap.apply(spendingByMonthExceptional).values()

        and:
        Try<Option<Double>> average = withoutFailure // hint: sequence, map, average
        Try<Option<Double>> firstFailure = withFailure // hint: sequence, map, average

        then:
        average.success
        average.get() == Option.some(6.5D)
        and:
        firstFailure.failure
        firstFailure.cause.class == RuntimeException
        firstFailure.cause.message == "Expenses in March cannot be loaded."
    }

    def "parse number: if success - square it, otherwise do nothing"() {
        given:
        Function<String, Integer> parse = { Integer.parseInt(it) }
        def number = '2'
        def letter = 'a'

        when:
        Try<Integer> squared = number // try to parse number here, then square, hint: map
        Try<Integer> fail = letter // try to parse number here, then square, hint: map

        then:
        squared.success
        squared.get() == 4
        and:
        fail.failure
        fail.cause.class == NumberFormatException
    }

    def "get person from database, and then try to estimate income"() {
        given:
        Function<Person, Integer> estimateIncome = {
            switch (it.id) {
                case 1:
                    throw new CannotEstimateIncome()
                default:
                    return 30
            }
        }
        and:
        def personWithIncome = 2
        def personWithoutIncome = 1

        when:
        Try<Integer> withIncome = personWithIncome // hint: PersonRepository.findById, map, estimateIncome
        Try<Integer> withoutIncome = personWithoutIncome // hint: PersonRepository.findById, map, estimateIncome

        then:
        withIncome.success
        withIncome.get() == 30
        withoutIncome.failure
        withoutIncome.cause.class == CannotEstimateIncome
    }

    def "get person from database, and then try to estimate income wrapped with Try"() {
        given:
        Function<Person, Try<Integer>> estimateIncome = {
            switch (it.id) {
                case 1:
                    return Try.failure(new CannotEstimateIncome())
                default:
                    return Try.success(30)
            }
        }
        and:
        def personWithoutIncome = 1
        def personWithIncome = 2

        when:
        Try<Integer> withIncome = personWithIncome // hint: PersonRepository.findById, flatMap, estimateIncome
        Try<Integer> withoutIncome = personWithoutIncome // hint: PersonRepository.findById, flatMap, estimateIncome

        then:
        withIncome.success
        withIncome.get() == 30
        withoutIncome.failure
        withoutIncome.cause.class == CannotEstimateIncome
    }

    def "performing side-effects: try to parse a number; if success - increment counter, otherwise do nothing"() {
        given:
        Function<String, Integer> parse = { Integer.parseInt(it) }
        def number = '2'
        def letter = 'a'
        def successCounter = 0

        when:
        Try.of { parse.apply(number) } // increment counter here, hint: andThen
        Try.of { parse.apply(letter) } // increment counter here, hint: andThen

        then:
        successCounter == 1
    }

    def "performing side-effects: on failure increment failure counter, on success increment success counter"() {
        given:
        def failureCounter = 0
        def successCounter = 0
        def existingId = 1
        def databaseConnectionProblem = 2

        when:
        DatabaseRepository.findById(existingId) // increment counter here, hint: onSuccess
        DatabaseRepository.findById(databaseConnectionProblem) // increment counter here, hint: onFailure

        then:
        successCounter == 1
        failureCounter == 1
    }

    def "performing side-effects: difference between onSuccess and andThen - exceptions in onSuccess"() {
        when:
        Try.success(1).onSuccess { throw new RuntimeException() }

        then:
        -1 // verify that RuntimeException was thrown and not caught, hint: thrown()
    }

    def "performing side-effects: difference between onSuccess and andThen - exceptions in andThen"() {
        when:
        def tried = Try.success(1).andThen { throw new RuntimeException() }

        then:
        -1 // verify failure, hint: isFailure
        -1 // verify that RuntimeException was caught, hint: getCause, class
    }

    def "performing side-effects: get person from database, change age and then try to save"() {
        given:
        def canBeSavedId = 1
        def userModifiedId = 2
        def connectionProblemId = 3
        def fakeId = 4

        and:
        Consumer<Person> saveToDatabase = {
            PersonRepository.save(it)
        }

        when:
        Try<Person> tried1 = PersonRepository.findById(canBeSavedId) // change age and try to save, hint: map, andThen
        and:
        Try<Person> tried2 = PersonRepository.findById(userModifiedId) // change age and try to save, hint: map, andThen
        and:
        Try<Person> tried3 = PersonRepository.findById(connectionProblemId) // change age and try to save, hint: map, andThen
        and:
        Try<Person> tried4 = PersonRepository.findById(fakeId) // change age and try to save, hint: map, andThen

        then:
        tried1.success
        and:
        tried2.failure
        tried2.cause.class == PersonModifiedInMeantimeException
        and:
        tried3.failure
        tried3.cause.class == DatabaseConnectionProblem
        and:
        tried4.failure
        tried4.cause.class == EntityNotFoundException
    }

    def "try to parse number, then map value with a partial function; if not defined -> NoSuchElementException"() {
        given:
        Function<String, Integer> parse = { Integer.parseInt(it) }
        String zero = '0'
        String two = '2'

        and:
        PartialFunction<Integer, Integer> div = Function1.of { 5 / it }
                .partial { it != 0 }
        PartialFunction<Integer, Integer> add = Function1.of { 5 + it }
                .partial { true }

        when:
        Try<Integer> summed = Try.of { parse.apply(two) } // convert here, hint: collect, use add()
        Try<Integer> dived = Try.of { parse.apply(zero) } // convert here, hint: collect, use div()

        then:
        summed.success
        summed.get() == 7
        and:
        dived.failure
        dived.cause.class == NoSuchElementException
        dived.cause.message == 'Predicate does not hold for 0'
    }

    def "if value > 2 do nothing, otherwise failure with NoSuchElementException"() {
        given:
        Predicate<Integer> moreThanTwo = { it > 2 }
        Try<Integer> three = Try.of { 3 }
        Try<Integer> two = Try.of { 2 }

        when:
        Try<Integer> filteredThree = three // filter here using moreThanTwo, hint: filter
        Try<Integer> filteredTwo = two // filter here using moreThanTwo, hint: filter

        then:
        filteredThree.success
        filteredThree.get() == 3
        and:
        filteredTwo.failure
        filteredTwo.cause.class == NoSuchElementException
        filteredTwo.cause.message == 'Predicate does not hold for 2'
    }

    def "if person.isAdult do nothing, otherwise failure with customized error - NotAnAdultException"() {
        given:
        def adult = Person.builder().age(20).build()
        def kid = Person.builder().age(10).build()
        Try<Person> adultTry = Try.of { adult }
        Try<Person> kidTry = Try.of { kid }

        and:
        Supplier<NotAnAdultException> exceptionSupplier = {
            new NotAnAdultException()
        }

        when:
        Try<Person> filteredAdult = adultTry  // filter here using exceptionSupplier, hint: filter
        Try<Person> filteredKid = kidTry  // filter here using exceptionSupplier, hint: filter

        then:
        filteredAdult.success
        filteredAdult.get() == adult
        and:
        filteredKid.failure
        filteredKid.cause.class == NotAnAdultException
    }

    def "try to find in cache, if not found - then try to find in database"() {
        given:
        def fromDatabaseId = 4
        def fromCacheId = 20
        def databaseConnectionProblemId = 2

        and:
        /*
        implement function: findById using: CacheRepository.findById, DatabaseRepository.findById, hint: orElse
         */
        Function<Integer, Try<String>> findById = { Try.success(1) }

        when:
        Try<String> fromDatabase = findById.apply(fromDatabaseId)
        Try<String> fromCache = findById.apply(fromCacheId)
        Try<String> backupConnectionProblem = findById.apply(databaseConnectionProblemId)

        then:
        fromDatabase.success
        fromDatabase.get() == 'from database'
        and:
        fromCache.success
        fromCache.get() == 'from cache'
        and:
        backupConnectionProblem.failure
        backupConnectionProblem.cause.class == DatabaseConnectionProblem
    }

    def "recovery: if DatabaseConnectionProblem, recover with default response"() {
        given:
        def defaultResponse = 'default response'
        def databaseConnectionError = 2
        def realId = 1

        when:
        Try<String> byIdSuccess = DatabaseRepository.findById(realId) // recover here with defaultResponse, hint: recover
        Try<String> byIdRecovered = DatabaseRepository.findById(databaseConnectionError) // recover here with defaultResponse, hint: recover

        then:
        byIdSuccess.success
        byIdSuccess.get() == 'from database'
        and:
        byIdRecovered.success
        byIdRecovered.get() == defaultResponse
    }

    def "recovery: if DatabaseConnectionProblem, recover with responseFunction"() {
        given:
        def databaseConnectionError = 2
        def realId = 1
        Function<DatabaseConnectionProblem, String> responseFunction = {
            "default response: ${it}"
        }

        when:
        Try<String> byIdSuccess = DatabaseRepository.findById(realId) // recover here with responseFunction, hint: recover
        Try<String> byIdRecovered = DatabaseRepository.findById(databaseConnectionError) // recover here with responseFunction, hint: recover

        then:
        byIdSuccess.success
        byIdSuccess.get() == 'from database'
        and:
        byIdRecovered.success
        byIdRecovered.get() == 'default response: DatabaseConnectionProblem(userId=2)'
    }

    def "recovery: if DatabaseConnectionProblem recover with request to the other (backup) database"() {
        given:
        def databaseConnectionError = 2
        def realId = 1

        when:
        /*
        use: DatabaseConnectionProblem (has field with userId), BackupRepository.findById, hint: recoverWith
         */
        Try<String> byIdSuccess = DatabaseRepository.findById(realId) // start chaining here
        Try<String> byIdRecovered = DatabaseRepository.findById(databaseConnectionError) // start chaining here

        then:
        byIdSuccess.success
        byIdSuccess.get() == 'from database'
        and:
        byIdRecovered.success
        byIdRecovered.get() == 'from backup'
    }

    def "recovery: CacheSynchronization/DatabaseConnectionProblem - default answer, CacheUserCannotBeFound - database request"() {
        given:
        def userFromCache = 1
        def databaseConnection = 2
        def userFromDatabase = 4
        def cacheSynchronization = 5

        when:
        Function<Integer, Try<String>> findById = {
            CacheRepository.findById(it)
            // use: CacheSynchronization, 'cache synchronization with database, try again later', hint: recover
            // use: CacheUserCannotBeFound, DatabaseRepository.findById(it.getUserId()), hint: recoverWith
            // use: DatabaseConnectionProblem, 'cannot connect to database', hint: recover
        }

        then:
        findById.apply(userFromCache).get() == 'from cache'
        findById.apply(userFromDatabase).get() == 'from database'
        findById.apply(cacheSynchronization).get() == 'cache synchronization with database, try again later'
        findById.apply(databaseConnection).get() == 'cannot connect to database'
    }

    def "vavr try-finally"() {
        given:
        def counter = 0
        def increment = { counter++ }
        def throwException = 1
        def success = 2
        and:
        Function<Integer, Integer> operation = {
            if (it == throwException) {
                throw new RuntimeException()
            }
            it
        }

        when:
        // perform increment regardless failure (always), hint: andFinally, use increment
        Try.of { operation.apply(throwException) }
        // perform increment regardless failure (always), hint: andFinally, use increment
        Try.of { operation.apply(success) }

        then:
        counter == 2
    }

    def "vavr try with resources: success"() {
        when:
        Try<String> concat = TWR.usingVavr('src/test/resources/lines.txt') // go to usingVavr and implement the method

        then:
        concat.success
        concat.get() == '1,2,3'
    }

    def "vavr try with resources: failure - file does not exists"() {
        when:
        Try<String> concat = TWR.usingVavr('404.txt') // go to usingVavr and implement the method

        then:
        concat.failure
        concat.cause.class == NoSuchFileException
        concat.cause.message == '404.txt'
    }

    def "pattern matching: map third-party library exceptions to domain exceptions with same message"() {
        given:
        Try<Integer> fail = Try.of { Integer.parseInt('a') }

        and:
        Function<Throwable, CannotParseInteger> mapper = { new CannotParseInteger(it.message) }
        
        when:
        /*
        use: pattern matching, NumberFormatException, mapper
        hint: mapFailure, Case, $, instanceOf
         */
        Try<Integer> mapped = fail

        then:
        mapped.failure
        mapped.cause.class == CannotParseInteger
    }
}