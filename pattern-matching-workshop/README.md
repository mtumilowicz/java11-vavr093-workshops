[![Build Status](https://travis-ci.com/mtumilowicz/java11-vavr093-pattern-matching-workshop.svg?branch=master)](https://travis-ci.com/mtumilowicz/java11-vavr093-pattern-matching-workshop)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# java11-vavr093-pattern-matching-workshop

# project description
* https://www.vavr.io/vavr-docs/#_pattern_matching
* https://static.javadoc.io/io.vavr/vavr/0.9.3/io/vavr/Predicates.html
* https://static.javadoc.io/io.vavr/vavr/0.9.3/io/vavr/Patterns.html
* in the workshop we will try to rewrite all methods from `java/workshops/Workshop` using pattern matching
    * don't forget to implement methods from `java/workshops/DecompositionWorkshop`
* answers: `java/workshops/Answers`

# theory in a nutshell
* easy example to set intuition
    ```
    int i = ...
    String s = Match(i).of(
        Case($(1), "one"),
        Case($(2), "two"),
        Case($(), "?")
    );
    ```
    and a lazy equivalent with functions
    ```
    int i = ...
    String s = Match(i).of(
        Case($(1), number -> "one"),
        Case($(2), number -> "two"),
        Case($(), number -> "?")
    );
    ```
* match is type-safe, so in above examples typing
    ```
    int i = ...
    String s = Match(i).of(
        Case($("1"), number -> "one"), // String instead of Integer
        Case($(new Object()), number -> "two") // Object instead of Integer
    );
    ```
    will cause compilation errors
* `switch-case` on steroids
* saves us from writing piles of nested if-then-else
* is not natively supported in Java - we need third party libraries
    * is natively supported in many languages - `Scala`, `Haskell`, `Kotlin`
* more human readable way
* reduces the amount of code while focusing on the relevant parts
* `Match(i).of` is to some extent the equivalent of `switch` statement
* `Case` consists of two parts:
    * part that matches logic from predicate, for example: `$(1) ~ (i -> i == 1)`
    * part that tells what should be returned when value matches
        * returned value can be any object 
        * returned value can be a supplier of an object
        * returned value can be a function of a matched value
* `public interface Case<T, R> extends PartialFunction<T, R>`
    * domain is defined by mentioned above predicate
* patterns overview
    * `$()` - wildcard pattern
        * saves us from a `MatchError` which is thrown if no case matches
    * `$(value)` - equals pattern
    * `$(predicate)` - conditional pattern
* in most cases it acts like an expression, it results in a value, but there is a
possibility to handle side effects as well (well-known `Void` hack)
* a constructor is a function which is applied to arguments and returns a new instance, 
a deconstructor is a function which takes an instance and returns the parts (object is unapplied)
* example of deconstruction - `LocalDate -> (year, month, day)`
* structural pattern matching allows us to deconstruct object hierarchies
    ```
    return Match(date).of(
            Case($LocalDate($(2019), $(), $()), (year, month, day) -> ...),
            Case($LocalDate($(2018), $(), $()), ...),
            Case($LocalDate($(2017), $(), $()), ...)
    );
    ```

# conclusions in a nutshell
* as we cannot perform exhaustiveness checks (feature not supporter on the language level), 
there is possibility to return an `Option` result which prevents as from `MatchError`:
    ```
    Match(obj).option(
         Case($(predicate1()), ...));
         Case($(predicate2()), ...));
    ```
    * and in the case when there is no match for `predicate1()` and `predicate2()`
    we got `Option.none()` instead of `MatchError`
* predefined `Predicates`

    |method   |description   |
    |---|---|
    |`allOf(Predicate<T>... predicates)`          |A combinator that checks if all of the given predicates are satisfied.   |
    |`anyOf(Predicate<T>... predicates)`          |A combinator that checks if at least one of the given predicates is satisfies.   |
    |`noneOf(Predicate<T>... predicates)`         |A combinator that checks if none of the given predicates is satisfied.   |
    |`exists(Predicate<? super T> predicate)`     |A combinator that checks if one or more elements of an `Iterable` satisfy the predicate.   |
    |`forAll(Predicate<? super T> predicate)`     |A combinator that checks if all elements of an `Iterable` satisfy the predicate.   |
    |`instanceOf(Class<? extends T> type)`        |Creates a `Predicate` that tests, if an object is instance of the specified type.   |
    |`is(T value)`                                |Creates a `Predicate` that tests, if an object is equal to the specified value using  `Objects.equals(Object, Object)` for comparison.   |
    |`isIn(T... values)`                          |Creates a `Predicate` that tests, if an object is equal to at least one of the specified values using `Objects.equals(Object, Object)` for comparison.   |
    |`isNotNull()`                                |Creates a `Predicate` that tests, if an object is not `null`   |
    |`isNull()`                                   |Creates a `Predicate` that tests, if an object is `null`   |
* structural pattern matching (object decomposition using `Patterns`)
    ```
    Try<Integer> _try = Try.of(() -> Integer.parseInt(number));
    Match(_try).of(
            Case($Success($()), i -> ...),
            Case($Failure($()), ex -> ...)
    );
    ```
* predefined `Patterns`

    |method   |purpose   |
    |---|---|
    |`$Cons(API.Match.Pattern<_1,?> p1, API.Match.Pattern<_2,?> p2)`        |`List`   |
    |`$Nil()`                                  |`List`  |
    |`$Success(API.Match.Pattern<_1,?> p1)`    |`Try`  |
    |`$Failure(API.Match.Pattern<_1,?> p1)`    |`Try`  |
    |`$Future(API.Match.Pattern<_1,?> p1)`     |`Future`  |
    |`$Invalid(API.Match.Pattern<_1,?> p1)`    |`Validation`  |
    |`$Valid(API.Match.Pattern<_1,?> p1)`      |`Validation`  |
    |`$Left(API.Match.Pattern<_1,?> p1)`       |`Either`  |
    |`$Right(API.Match.Pattern<_1,?> p1)`      |`Either`  |
    |`$Some(API.Match.Pattern<_1,?> p1)`       |`Option`  |
    |`$None()`                                 |`Option` |
    |`$TupleN(N...)`                           |`TupleN`  |
* user-defined patterns
    * generated by annotation (pre)processor and stored under 
    `build/generated/sources/annotationProcessor` (gradle)
    * implemented by us in `workshops.DecompositionAnswers`
        ```
        static Tuple3<Integer, Integer, Integer> LocalDate(LocalDate date) {
            return Tuple.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        }
        ```
    * then generated by annotation processor
        ```
        public static <_1 extends Integer, _2 extends Integer, _3 extends Integer> Pattern3<LocalDate, _1, _2, _3> $LocalDate(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3) {
            return Pattern3.of(LocalDate.class, p1, p2, p3, workshops.DecompositionAnswers::LocalDate);
        }
        ```
    * and ready to be used
        ```
        return Match(date).of(
                Case($LocalDate($(y -> y < 2000), $(), $()), return something for pre 2000),
                Case($LocalDate($(y -> y >= 2000), $(), $()), return something for after 2000)
        );
        ```
    
* performing side effects
    ```
    Match(PersonRepository.findById(id)).of(
            Case($None(), run(() -> display.push("cannot find person with id = " + id))),
            Case($Some($()), value -> run(() -> display.push("person: " + value + " processed")))
    );
    ```
    * `run` must not be used as direct return value, i.e. outside of a lambda body - 
    the cases have to be evaluated lazily