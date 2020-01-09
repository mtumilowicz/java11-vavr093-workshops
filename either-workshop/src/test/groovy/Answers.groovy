import io.vavr.collection.HashMap
import io.vavr.collection.List
import io.vavr.collection.Map
import io.vavr.collection.Seq
import io.vavr.control.Either
import io.vavr.control.Option
import io.vavr.control.Try
import spock.lang.Specification

import java.time.Month
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.UnaryOperator
import java.util.stream.Collectors

class Answers extends Specification {

    def "create successful (Right) Either with value 1"() {
        given:
        Either<String, Integer> success = Either.right(1)

        expect:
        success.isRight()
        success.get() == 1
    }

    def "create failed (Left) Either with message: 'wrong status'"() {
        given:
        Either<String, Integer> fail = Either.left('wrong status')

        expect:
        fail.isLeft()
        fail.getLeft() == 'wrong status'
    }

    def "conversion: Option -> Either"() {
        given:
        Option<Integer> some = Option.some(1)
        Option<Integer> none = Option.none()
        def message = 'option was empty'

        when:
        Either<String, Integer> eitherFromSome = some.toEither(message)
        Either<String, Integer> eitherFromNone = none.toEither(message)

        then:
        eitherFromSome == Either.right(1)
        eitherFromNone == Either.left(message)
    }

    def "conversion: Either -> Option"() {
        given:
        Either<String, Integer> right = Either.right(1)
        Either<String, Integer> left = Either.left('no data')

        when:
        Option<Integer> optionFromRight = right.toOption()
        Option<Integer> optionFromLeft = left.toOption()

        then:
        optionFromRight == Option.some(1)
        optionFromLeft == Option.none()
    }

    def "conversion: Try -> Either"() {
        given:
        def exception = new IllegalArgumentException('wrong input data')
        Try<Integer> success = Try.success(1)
        Try<Integer> failure = Try.failure(exception)

        when:
        Either<Throwable, Integer> eitherFromSuccess = success.toEither()
        Either<Throwable, Integer> eitherFromFailure = failure.toEither()

        then:
        eitherFromSuccess == Either.right(1)
        eitherFromFailure == Either.left(exception)
    }

    def "conversion: Either -> Try"() {
        given:
        Either<String, Integer> right = Either.right(1)
        Either<String, Integer> left = Either.left('no data')

        when:
        Try<Integer> tryFromRight = right.toTry()
        Try<Integer> tryFromLeft = left.toTry()

        then:
        tryFromRight == Try.success(1)
        tryFromLeft.failure
        tryFromLeft.cause.class == NoSuchElementException
        tryFromLeft.cause.message == 'get() on Left'
    }

    def "sum values of right Eithers sequence or return the first failure"() {
        given:
        Either<String, Integer> n1 = Either.right(1)
        Either<String, Integer> n2 = Either.right(2)
        Either<String, Integer> n3 = Either.right(3)
        Either<String, Integer> n4 = Either.right(4)
        Either<String, Integer> failure1 = Either.left('cannot parse integer a')
        Either<String, Integer> failure2 = Either.left('cannot parse integer b')

        and:
        List<Either<String, Integer>> from1To4 = List.of(n1, n2, n3, n4)
        List<Either<String, Integer>> all = List.of(n1, n2, n3, n4, failure1, failure2)

        when:
        Either<String, Number> sum = Either.sequenceRight(from1To4)
                .map { it.sum() }
        Either<String, Number> fail = Either.sequenceRight(all)
                .map { it.sum() }

        then:
        sum.isRight()
        sum.get() == 10
        and:
        fail == Either.left('cannot parse integer a')
    }

    def "count average expenses in a year (by month) or aggregate failures"() {
        given:
        def spendingByMonth = {
            Either.right(it.getValue())
        }

        and:
        def spendingByMonthExceptional = {
            switch (it) {
                case Month.MARCH:
                    Either.left('Expenses in March cannot be loaded.')
                    break
                case Month.APRIL:
                    Either.left('Expenses in April cannot be loaded.')
                    break
                default: Either.right(it.getValue())
            }
        }

        and:
        Function<Function<Month, Integer>, Map<Month, Either<String, Integer>>> expensesByMonthMap = {
            spendingIn ->
                HashMap.ofAll(Arrays.stream(Month.values())
                        .collect(Collectors.toMap(
                                Function.identity(),
                                { month -> spendingIn(month) })))
        }

        when:
        Seq<Either<String, Integer>> withoutFailure = expensesByMonthMap.apply(spendingByMonth).values()
        Seq<Either<String, Integer>> withFailures = expensesByMonthMap.apply(spendingByMonthExceptional).values()

        and:
        Either<Seq<String>, Option<Double>> average = Either.sequence(withoutFailure)
                .map { it.average() }
        Either<Seq<String>, Option<Double>> failures = Either.sequence(withFailures)
                .map { it.average() }

        then:
        average == Either.right(Option.some(6.5D))
        and:
        failures.isLeft()
        failures.getLeft().size() == 2
        failures.getLeft().containsAll('Expenses in March cannot be loaded.', 'Expenses in April cannot be loaded.')
    }

    def "square the left side or cube the right side"() {
        given:
        Either<Integer, Integer> left = Either.left(2)
        Either<Integer, Integer> right = Either.right(3)
        and:
        Function<Integer, Integer> square = { it**2 }
        Function<Integer, Integer> cube = { it**3 }

        when:
        Either<Integer, Integer> leftBimapped = left.bimap(square, cube)
        Either<Integer, Integer> rightBimapped = right.bimap(square, cube)

        then:
        leftBimapped == Either.left(4)
        rightBimapped == Either.right(27)
    }

    def "square and get the left side or cube and get the right side"() {
        given:
        Either<Integer, Integer> left = Either.left(2)
        Either<Integer, Integer> right = Either.right(3)
        and:
        Function<Integer, Integer> square = { it**2 }
        Function<Integer, Integer> cube = { it**3 }

        when:
        Integer leftFolded = left.fold(square, cube)
        Integer rightFolded = right.fold(square, cube)

        then:
        leftFolded == 4
        and:
        rightFolded == 27
    }

    def "count square root for x >=0 wrapped as an either"() {
        given:
        Either<String, Integer> negative = Either.right(-1)
        Either<String, Integer> positive = Either.right(1)
        Either<String, Integer> failure = Either.left("no data")

        and:
        Function<Either<String, Integer>, Option<Either<String, Double>>> square = {
            it.filter { it >= 0 }
                    .map { it.map { Math.sqrt(it) } }
        }

        expect:
        square.apply(negative) == Option.none()
        square.apply(positive) == Option.some(Either.right(1D))
        square.apply(failure) == Option.some(Either.left('no data'))
    }

    def "get person from database, and then estimate income wrapped with Either"() {
        given:
        Function<Person, Either<String, Integer>> estimateIncome = {
            switch (it.id) {
                case 1:
                    return Either.left("cannot estimate income for person = ${it.id}")
                default:
                    return Either.right(30)
            }
        }
        and:
        def personWithIncome = 2
        def personWithoutIncome = 1

        when:
        Either<String, Integer> withIncome = PersonRepository.findById(personWithIncome)
                .flatMap { estimateIncome.apply(it) }
        Either<String, Integer> withoutIncome = PersonRepository.findById(personWithoutIncome)
                .flatMap { estimateIncome.apply(it) }

        then:
        withIncome == Either.right(30)
        withoutIncome == Either.left("cannot estimate income for person = ${personWithoutIncome}")
    }

    def "square the right side, if left - do nothing"() {
        given:
        Either<Integer, Integer> right = Either.right(2)
        Either<Integer, Integer> left = Either.left(2)

        Function<Integer, Integer> square = { it**2 }

        when:
        Either<Integer, Integer> rightSquared = right.map(square)
        Either<Integer, Integer> leftUntouched = left.map(square)

        then:
        rightSquared == Either.right(4)
        leftUntouched == Either.left(2)
    }

    def "square the left side, if right - do nothing"() {
        given:
        Either<Integer, Integer> right = Either.right(2)
        Either<Integer, Integer> left = Either.left(2)

        Function<Integer, Integer> square = { it**2 }

        when:
        Either<Integer, Integer> rightUntouched = right.mapLeft(square)
        Either<Integer, Integer> leftSquared = left.mapLeft(square)

        then:
        rightUntouched == Either.right(2)
        leftSquared == Either.left(4)
    }

    def "try to find in cache, if failure - try to find in database"() {
        given:
        def fromDatabaseId = 2
        def fromCacheId = 1
        def nonexistentId = 3

        and:
        Function<Integer, Either<String, String>> findById = {
            id -> CacheRepository.findById(id).orElse { DatabaseRepository.findById(id) }
        }

        when:
        Either<String, String> fromDatabase = findById.apply(fromDatabaseId)
        Either<String, String> fromCache = findById.apply(fromCacheId)
        Either<String, String> nonexistent = findById.apply(nonexistentId)

        then:
        fromDatabase == Either.right('from database, id = 2')
        fromCache == Either.right('from cache, id = 1')
        nonexistent == Either.left('user cannot be found in database, id = 3')
    }

    def "performing side-effects: try to find in cache, if failure - try to find in database, log every failure"() {
        given:
        def logfile = []
        def fromDatabaseId = 2
        def fromCacheId = 1
        def nonexistentId = 3

        and:
        Consumer<Integer> log = {
            logfile << it
        }

        and:
        Function<Integer, Either<String, String>> findById = {
            id ->
                CacheRepository.findById(id)
                        .peekLeft(log)
                        .orElse { DatabaseRepository.findById(id) }
                        .peekLeft(log)
        }

        when:
        Either<String, String> fromDatabase = findById.apply(fromDatabaseId)
        Either<String, String> fromCache = findById.apply(fromCacheId)
        Either<String, String> nonexistent = findById.apply(nonexistentId)

        then:
        fromDatabase == Either.right('from database, id = 2')
        fromCache == Either.right('from cache, id = 1')
        nonexistent == Either.left('user cannot be found in database, id = 3')
        and:
        logfile.size() == 3
        logfile == ["user cannot be found in cache, id = ${fromDatabaseId}",
                    "user cannot be found in cache, id = ${nonexistentId}",
                    "user cannot be found in database, id = ${nonexistentId}"]
    }

    def "performing side-effects: log success"() {
        given:
        def logfile = []
        def fromDatabaseId = 2
        def nonexistentId = 3

        and:
        Consumer<Integer> log = {
            logfile << it
        }

        and:
        Function<Integer, Either<String, String>> findById = {
            id -> DatabaseRepository.findById(id).peek(log)
        }

        when:
        findById.apply(nonexistentId)
        findById.apply(fromDatabaseId)

        then:
        logfile == ["from database, id = ${fromDatabaseId}"]
    }

    def "performing side-effects: if left push message to the display"() {
        given:
        def display = []
        def fromDatabaseId = 2
        def nonexistentId = 3

        and:
        Consumer<Integer> pushToDisplay = {
            display << it
        }

        and:
        Consumer<Integer> process = {
            id ->
                DatabaseRepository.findById(id)
                        .orElseRun(pushToDisplay)
        }

        when:
        process.accept(nonexistentId)
        process.accept(fromDatabaseId)

        then:
        display == ["user cannot be found in database, id = ${nonexistentId}"]
    }

    def "implement bimap using only map and swap"() {
        given:
        def message = 'no data'
        Either<String, Integer> left = Either.left(message)
        Either<String, Integer> right = Either.right(2)
        UnaryOperator<String> lmap = { "sorry: ${message}" }
        UnaryOperator<Integer> rmap = { it**2 }

        when:
        Either<String, Integer> leftBimapped = BiMapperAnswer.bimap(left, lmap, rmap)
        Either<String, Integer> rightBimapped = BiMapperAnswer.bimap(right, lmap, rmap)

        then:
        leftBimapped == Either.left("sorry: ${message}")
        and:
        rightBimapped == Either.right(4)
    }

    def "combo"() {
        given:
        PersonRequest pr1 = PersonRequest.builder().id(1).age(20).build()
        PersonRequest pr2 = PersonRequest.builder().id(2).age(22).build()
        PersonRequest pr3 = PersonRequest.builder().id(3).age(22).build()

        and:
        Function<PersonRequest, Either<String, Person>> parse = {
            PersonRequestMapper.toPerson(it)
                    .peek(PersonServiceAnswer.updateStats)
                    .flatMap { PersonServiceAnswer.process(it) }
        }

        expect:
        parse.apply(pr1) == Either.left('stats <= 15')
        parse.apply(pr2) == Either.right(Person.builder()
                .id(2)
                .age(22)
                .active(true)
                .build())
        parse.apply(pr3) == Either.left('cannot load stats for person = 3')
    }
}
