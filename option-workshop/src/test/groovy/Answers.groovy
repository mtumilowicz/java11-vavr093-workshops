import io.vavr.Function1
import io.vavr.PartialFunction
import io.vavr.collection.HashSet
import io.vavr.collection.List
import io.vavr.control.Option
import spock.lang.Specification

import java.util.function.Function
import java.util.function.Supplier

import static java.util.Objects.nonNull

/**
 * Created by mtumilowicz on 2019-03-02.
 */
class Answers extends Specification {

    def "create empty option"() {
        given:
        Option none = Option.none()

        expect:
        none.isEmpty()
    }

    def "create not empty option with not null value"() {
        given:
        Option<Integer> some = Option.some(5)

        expect:
        some.isDefined()
        some.get()
    }

    def "create not empty option with null value"() {
        given:
        Option<Integer> some = Option.some()

        expect:
        some.isDefined()
        !some.get()
    }

    def "check if option is empty / not empty"() {
        given:
        def empty = Option.none()
        def notEmpty = Option.some()

        expect:
        empty.isEmpty()
        notEmpty.isDefined()
    }

    def "conversion: optional -> option"() {
        given:
        Optional<Integer> emptyOptional = Optional.empty()
        Optional<Integer> notEmptyOptional = Optional.of(1)

        when:
        Option<Integer> emptyOption = Option.ofOptional(emptyOptional)
        Option<Integer> notEmptyOption = Option.ofOptional(notEmptyOptional)

        then:
        emptyOption == Option.none()
        notEmptyOption == Option.some(1)
    }

    def "conversion: option -> optional"() {
        given:
        Option<Integer> emptyOption = Option.none()
        Option<Integer> notEmptyOption = Option.some(1)

        when:
        Optional<Integer> emptyOptional = emptyOption.toJavaOptional()
        Optional<Integer> notEmptyOptional = notEmptyOption.toJavaOptional()

        then:
        emptyOptional == Optional.empty()
        notEmptyOptional == Optional.of(1)
    }

    def "sum values of option sequence or return empty if any is empty"() {
        given:
        Option<Integer> value1 = Option.some(1)
        Option<Integer> value2 = Option.some(3)
        Option<Integer> value3 = Option.some(5)
        Option<Integer> value4 = Option.none()

        and:
        List<Option<Integer>> from1To3 = List.of(value1, value2, value3)
        List<Option<Integer>> all = List.of(value1, value2, value3, value4)

        when:
        Option<Number> sum = Option.sequence(from1To3)
                .map { it.sum() }
        Option<Number> empty = Option.sequence(all)
                .map { it.sum() }

        then:
        sum.defined
        sum.get() == 9
        empty == Option.none()
    }

    def "load additional data only when person.isAdult"() {
        given:
        def adult = new Person(25)
        def kid = new Person(10)
        Supplier<AdditionalData> loader = { new AdditionalData() }

        when:
        Option<AdditionalData> forAdult = Option.when(adult.isAdult(), loader)
        Option<AdditionalData> forKid = Option.when(kid.isAdult(), loader)

        then:
        forAdult.isDefined()
        forAdult.get().data == 'additional data'
        forKid.isEmpty()
    }

    def "map value with a partial function; if not defined -> Option.none()"() {
        given:
        Option<Integer> zero = Option.some(0)

        and:
        PartialFunction<Integer, Integer> div = Function1.of { 5 / it }
                .partial { it != 0 }
        PartialFunction<Integer, Integer> add = Function1.of { 5 + it }
                .partial { true }

        when:
        Option<Integer> dived = zero.collect(div)
        Option<Integer> summed = zero.collect(add)

        then:
        dived == Option.none()
        summed == Option.some(5)
    }

    def "if empty - run action, otherwise do nothing"() {
        given:
        Option<Integer> empty = Option.none()
        Option<Integer> notEmpty = Option.some(5)

        and:
        def counter = 0

        and:
        Runnable action = { counter++ }

        when:
        empty.onEmpty(action)
        notEmpty.onEmpty(action)

        then:
        counter == 1
    }

    def "if option value is an adult - do nothing, otherwise empty"() {
        given:
        Option<Person> adult = Option.some(new Person(20))
        Option<Person> kid = Option.some(new Person(15))

        when:
        Option<Person> checkedAdult = adult.filter { it.isAdult() }
        Option<Person> checkedKid = kid.filter { it.isAdult() }

        then:
        checkedAdult == adult
        checkedKid == Option.none()
    }

    def "find in cache, otherwise try to find in the database, otherwise empty"() {
        given:
        def fromCacheId = 1
        def fromDatabaseId = 2
        def fakeId = 3

        when:
        def fromCache = FacadeRepositoryAnswer.findById(fromCacheId)
        def fromDatabase = FacadeRepositoryAnswer.findById(fromDatabaseId)
        def notFound = FacadeRepositoryAnswer.findById(fakeId)

        then:
        Option.some('from cache') == fromCache
        Option.some('from database') == fromDatabase
        Option.none() == notFound
    }

    def "throw IllegalStateException if option is empty, otherwise get value"() {
        given:
        def empty = Option.none()

        when:
        empty.getOrElseThrow { new IllegalStateException() }

        then:
        thrown(IllegalStateException)
    }

    def "square value then convert to String, if empty - do nothing, null should be treated as 0"() {
        given:
        Option<Integer> defined = Option.some(2)
        Option<Integer> definedNull = Option.some()
        Option<Integer> empty = Option.none()
        and:
        Function<Integer, Integer> squareOrZero = { nonNull(it) ? it ** 2 : 0 }

        when:
        Option<String> definedMapped = defined.map { squareOrZero.apply(it) }
                .map { it.toString() }
        Option<String> definedNullMapped = definedNull.map { squareOrZero.apply(it) }
                .map { it.toString() }
        Option<String> emptyMapped = empty.map { squareOrZero.apply(it) }
                .map { it.toString() }

        then:
        definedMapped.defined
        definedMapped.get() == '4'
        definedNullMapped.defined
        definedNullMapped.get() == '0'
        emptyMapped.empty
    }

    def "Option.map vs Optional.map"() {
        given:
        Option<Integer> option = Option.some(2)
        Optional<Integer> optional = Optional.of(2)
        Function<Integer, Integer> alwaysNull = { null }

        when:
        Option<Integer> optionMapped = option.map(alwaysNull)
        Optional<Integer> optionalMapped = optional.map(alwaysNull)

        then:
        // option
        optionMapped.isDefined()
        !optionMapped.get()
        // optional
        optionalMapped.isEmpty()

    }

    def "flatten Option: find engine for a given car id"() {
        given:
        def existingCarId = 1
        def notExistingCarId = 2

        when:
        Option<Engine> engineFound = CarRepository.findCarById(existingCarId)
                .flatMap { CarRepository.findEngineById(it.engineId) }
        Option<Engine> engineNotFound = CarRepository.findCarById(notExistingCarId)
                .flatMap { CarRepository.findEngineById(it.engineId) }

        then:
        engineFound == Option.some(new Engine(1))
        engineNotFound.empty
    }

    def "increment counter by option value"() {
        given:
        Option<Integer> empty = Option.none()
        Option<Integer> five = Option.some(5)

        and:
        def counter = 0

        when:
        empty.peek { counter = +it }
        five.peek { counter = +it }

        then:
        counter == 5
    }

    def "convert: Some<Integer> -> one element List<Integer>, None -> empty list"() {
        given:
        Option<Integer> empty = Option.none()
        Option<Integer> five = Option.some(5)

        when:
        List<Integer> transformedEmpty = empty.toList()
        List<Integer> transformerFive = five.toList()

        then:
        transformedEmpty == List.empty()
        transformerFive == List.of(5)
    }

    def "convert: one element List<Integer> -> Some<Integer>, empty list -> None"() {
        given:
        List<Integer> empty = List.empty()
        List<Integer> five = List.of(5)

        when:
        Option<Integer> transformedEmpty = empty.toOption()
        Option<Integer> transformerFive = five.toOption()

        then:
        transformedEmpty == Option.none()
        transformerFive == Option.some(5)
    }

    def "check if some inner object contains 7"() {
        given:
        def existing = 7
        def notExisting = 10
        def list = List.of(List.of(1, 2, 3), HashSet.of(4, 5), Option.some(existing))

        when:
        boolean exists = list.exists { it.contains(existing) }
        boolean notExists = list.exists { it.contains(notExisting) }

        then:
        exists
        !notExists
    }

    def "check if all values in inner objects are < 10"() {
        given:
        def list = List.of(List.of(1, 2, 3), HashSet.of(4, 5), Option.some(7))

        when:
        boolean lessThan10 = list.forAll { it.forAll { it < 10 } }

        then:
        lessThan10
    }

    def "function composition, monadic law; example of option.map(f g) = option.map(f).map(g)"() {
        given:
        Function<Integer, Integer> alwaysNull = { null }
        Function<Integer, String> safeToString = { nonNull(it) ? String.valueOf(it) : 'null' }
        Function<Integer, String> composition = alwaysNull.andThen(safeToString)

        expect:
        Optional.of(1).map(composition) != Optional.of(1).map(alwaysNull).map(safeToString)
        Optional.of(1).stream().map(composition).findAny() == Optional.of(1).stream()
                .map(alwaysNull)
                .map(safeToString)
                .findAny()
        Option.of(1).map(composition) == Option.of(1).map(alwaysNull).map(safeToString)
    }
}