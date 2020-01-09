import com.google.common.collect.Range
import io.vavr.Function1
import io.vavr.Function2
import io.vavr.PartialFunction
import io.vavr.control.Option
import io.vavr.control.Try
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.function.BinaryOperator
import java.util.function.Function
import java.util.stream.Stream

class Workshop extends Specification {

    def "define partial function on [0,...,3] in a manner: x -> x + 1 if x e [0,...,3], otherwise -1"() {
        given:
        // implement in Increment
        PartialFunction<Integer, Integer> increment = new Increment(Range.closed(0, 3))

        expect:
        increment.apply(-1) == -1
        increment.apply(0) == 1
        increment.apply(1) == 2
        increment.apply(2) == 3
        increment.apply(3) == 4
        increment.apply(4) == -1
    }

    def "define partial function: identity on [0,...,3]"() {
        given:
        // implement in RandomIdentity
        PartialFunction<Integer, Integer> randomIdentity = new RandomIdentity(Range.closed(0, 3))

        expect:
        randomIdentity.apply(0) == 0
        randomIdentity.apply(1) == 1
        randomIdentity.apply(2) == 2
        randomIdentity.apply(3) == 3
    }

    def "lifter - lifting partial function with Option - Increment, RandomIdentity"() {
        given:
        // implement Lifter.lift(partial function)
        Function<Integer, Option<Integer>> liftedIncrement = Lifter.lift(new Increment())
        Function<Integer, Option<Integer>> liftedRandomIdentity = Lifter.lift(
                new RandomIdentity(Range.closed(0, 3))
        )

        expect:
        liftedIncrement.apply(-1) == Option.none()
        liftedIncrement.apply(0) == Option.some(1)
        liftedIncrement.apply(1) == Option.some(2)
        liftedIncrement.apply(2) == Option.some(3)
        liftedIncrement.apply(3) == Option.some(4)
        liftedIncrement.apply(4) == Option.none()
        and:
        liftedRandomIdentity.apply(-1) == Option.none()
        liftedRandomIdentity.apply(0) == Option.some(0)
        liftedRandomIdentity.apply(1) == Option.some(1)
        liftedRandomIdentity.apply(2) == Option.some(2)
        liftedRandomIdentity.apply(3) == Option.some(3)
        liftedRandomIdentity.apply(4) == Option.none()
    }

    def "lifter - lift function with Option"() {
        given:
        Function<Integer, Integer> exceptionalIdentity = {
            switch (it) {
                case 1:
                    throw new IllegalArgumentException()
                case 2:
                    throw new IllegalStateException()
                default:
                    it
            }
        }

        when:
        // implement Lifter.lift(function)
        Function<Integer, Option<Integer>> lifted = Lifter.lift(exceptionalIdentity)

        then:
        lifted.apply(1) == Option.none()
        lifted.apply(2) == Option.none()
        lifted.apply(3) == Option.some(3)
    }

    def "lifter - lift function with Try"() {
        given:
        Function<Integer, Integer> exceptionalIdentity = {
            switch (it) {
                case 1:
                    throw new IllegalArgumentException()
                case 2:
                    throw new IllegalStateException()
                default:
                    it
            }
        }

        when:
        // implement Lifter.liftTry(function)
        Function<Integer, Try<Integer>> lifted = Lifter.liftTry(exceptionalIdentity)
        def one = lifted.apply(1)
        def two = lifted.apply(2)
        def three = lifted.apply(3)

        then:
        one.failure
        one.cause.class == IllegalArgumentException
        two.failure
        two.cause.class == IllegalStateException
        three == Try.success(3)
    }

    def "vavr - lifting function with Option: div"() {
        given:
        BinaryOperator<Integer> div = { a, b -> a.intdiv(b) }

        when:
        Function2<Integer, Integer, Option<Integer>> lifted = div // use FunctionN.lift

        then:
        lifted.apply(1, 0) == Option.none()
        lifted.apply(null, 2) == Option.none()
        lifted.apply(2, 0) == Option.none()
        lifted.apply(4, 2) == Option.some(2)
    }

    def "vavr - lifting function with Try: div"() {
        given:
        BinaryOperator<Integer> div = { a, b -> a.intdiv(b) }

        when:
        Function2<Integer, Integer, Try<Integer>> lifted = div // use FunctionN.liftTry

        then:
        lifted.apply(1, 0).failure
        lifted.apply(1, 0).cause.class == ArithmeticException
        lifted.apply(null, 2).failure
        lifted.apply(2, 0).cause.class == ArithmeticException
        lifted.apply(4, 2) == Try.success(2)
    }

    def "vavr - lifting function with Try: Repository.findById"() {
        given:
        def repo = new Repository()
        Function1<Integer, Try<User>> findById = repo.findById(it) // use FunctionN.liftTry

        expect:
        findById.apply(1) == Try.success(new User(1))
        findById.apply(2).failure
        findById.apply(2).cause.class == UserCannotBeFoundException
    }

    def "for a given list of users, activate users that can be active and save them - using function lifting with Option"() {
        given:
        ActiveUserRepository activeUserRepository = new ActiveUserRepository()
        and:
        def cannotBeActive = BlockedUser.builder()
                .id(1)
                .banDate(LocalDate.parse("2014-10-12"))
                .warn(15)
                .build()
        and:
        def canBeActive = BlockedUser.builder()
                .id(2)
                .banDate(LocalDate.parse("2016-10-12"))
                .warn(0)
                .build()
        and:
        def now = Clock.fixed(Instant.parse("2016-12-03T10:15:30Z"), ZoneId.systemDefault())

        when:
        /*
        process here, 
        hint: use Function1.lift, BlockedUser.activate(now), activeUserRepository.add
         */
        Stream.of(cannotBeActive, canBeActive)

        then:
        activeUserRepository.count() == 1
        activeUserRepository.containsAll([2])
    }

    def "for a given list of users, activate users that can be active and save them or generate report - using function lifting with Try"() {
        given:
        ActiveUserRepository activeUserRepository = new ActiveUserRepository()
        and:
        def cannotBeActive = BlockedUser.builder()
                .id(1)
                .banDate(LocalDate.parse("2014-10-12"))
                .warn(15)
                .build()
        and:
        def canBeActive = BlockedUser.builder()
                .id(2)
                .banDate(LocalDate.parse("2016-10-12"))
                .warn(0)
                .build()
        and:
        def now = Clock.fixed(Instant.parse("2016-12-03T10:15:30Z"), ZoneId.systemDefault())
        and:
        List<String> fails = []

        when:
        /*
        process here, reports should be aggregated in fails
        hint: use Function1.lift, BlockedUser.activate(now), Try.onSuccess, onFailure, activeUserRepository.add
         */
        Stream.of(cannotBeActive, canBeActive)
        then:
        activeUserRepository.count() == 1
        activeUserRepository.containsAll([2])
        fails == ["id = 1: warns has to be <= 10"]
    }
}