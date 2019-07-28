[![Build Status](https://travis-ci.com/mtumilowicz/java11-vavr093-either-workshop.svg?branch=master)](https://travis-ci.com/mtumilowicz/java11-vavr093-either-workshop)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# java11-vavr093-either-workshop

# project description
* https://www.vavr.io/vavr-docs/#_either
* https://static.javadoc.io/io.vavr/vavr/0.9.3/io/vavr/control/Either.html
* https://github.com/mtumilowicz/java11-vavr-either
* in the workshop we will try to fix failing test `test/groovy/Workshop`
* answers: `test/groovy/Answers` (same tests as in `Workshop` but correctly solved)

# theory in a nutshell
* `Either` represents a value of two possible types
    * is either a `Left` or a `Right`
* by convention the success case is `Right` and the failure is `Left`
* simple example
    ```
    Either<String, Integer> div(int x, int y) {
        return (y == 0) ? Either.left("cannot div by 0!") : Either.right(x / y);
    }
    ```
* models a computation that may either result in an error (for example returning some error message or even exception), 
or return a successfully computed value
* you can think about `Either` as a pair `(Left, Right) ~ (Object, Object)` that has either left or right value
* `Try<T>` ~ `Either<Throwable, T>`
* `Either` is a generalisation of `Try`
* `interface Either<L, R> extends Value<R>, Serializable`
    * `interface Value<T> extends Iterable<T>`
* two implementations:
    * `final class Left<L, R> implements Either<L, R>`
    * `final class Right<L, R> implements Either<L, R>`
    
# conclusions in a nutshell
* creating
    * success: `Either.right(...)`
    * failure: `Either.left(...)`
* conversion: `Either<L, R> <-> Option<R>`
    ```
    option.toEither(L left)
    either.toOption()
    ```
* conversion: `Either<L, R> <-> Try<R>`
    ```
    try.toEither()
    either.toTry()
    ```
    * `Left -> NoSuchElementException("get() on Left")`
* conversion: `List<Either<L, R>> -> Either<Seq<L>, Seq<R>>`
    * `Either.sequence(list)`
* conversion: `List<Either<L, R>> -> Either<L, Seq<R>>`
    * `Either.sequenceRight(list)`
    * if any of the `Either` is `Left`, returns first `Left`
* we could filter
    * `Option<Either<L, R>> filter(Predicate<? super R> predicate)`
* map, mapLeft, flatMap `Either<L, R>`
    * `Either<L, U> map(f: R -> U)`
    * `Either<L, U> flatMap(f: R -> Either<L, U>)`
    * `Either<U, R> mapLeft(f: L -> R)`
* bimap, fold `Either<L, R>`
    * `Either<X, Y> bimap(leftMapper: L -> X, rightMapper: R -> Y)`
    * `U fold(leftMapper: L -> U, rightMapper: R -> U)`
* lazy alternative
    * ` Either<L, R> orElse(Supplier<? extends Either<? extends L, ? extends R>> supplier)`
* performing side-effects
    * on success:
        * `Either<L, R> peek(Consumer<? super R> action)`
    * on failure:
        * `Either<L, R> peekLeft(Consumer<? super L> action)`
        * `void orElseRun(Consumer<? super L> action)`
