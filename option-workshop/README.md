[![Build Status](https://travis-ci.com/mtumilowicz/java11-vavr093-option-workshop.svg?branch=master)](https://travis-ci.com/mtumilowicz/java11-vavr093-option-workshop)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# option-workshop

# project description
* https://www.vavr.io/vavr-docs/#_option
* https://static.javadoc.io/io.vavr/vavr/0.9.3/io/vavr/control/Option.html
* https://github.com/mtumilowicz/java11-vavr-option
* https://github.com/mtumilowicz/java11-category-theory-optional-is-not-functor
* in the workshop we will try to fix failing tests `test/groovy/Workshop`
* answers: `test/groovy/Answers` (same tests as in `Workshop` but correctly solved)

# theory in a nutshell
* similar to `Optional`, but with bigger, more flexible API
* `interface Option<T> extends Value<T>, Serializable`
    * `interface Value<T> extends Iterable<T>`
* `Option` is isomorphic to singleton list (either has element or not, so it could be treated as collection)
* two implementations:
    * `final class Some<T>`
    * `final class None<T>`
* `None` instance is a singleton
* returning `null` inside `map` does not cause `Option` switch to `None` (contrary to `Optional`)
    ```
    expect:
    Option.some(2).map(alwaysNull).defined
    Optional.of(2).map(alwaysNull).empty
    ```
* it is possible to have `Some(null)`, so `NPE` is possible even on defined `Option`
* excellent for modelling exists / not exists (Spring Data - `findById`)
    * not every "exceptional" behaviour could be modelled as exists / not exists
    
# conclusions in a nutshell
* **we omit methods that `Optional` has**
* easy conversion `Option<T>` <-> `Optional<T>`
    * `Option.ofOptional`
    * `option.toJavaOptional()`
* handy conversion `List<Option<T>> -> Option<List<T>>`
    * `Option.sequence(list)`
    * if any of the `Options` are `None` - returns `None`
* conditional supplier
    * `static Option<T> when(boolean condition, Supplier<? extends T> supplier)`
* mapping with the partial function
    * `Option<R> collect(PartialFunction<? super T, ? extends R> partialFunction)`
    * if function is not defined for the value - returns `None`
* side effects on `None`
    * `Option<T> onEmpty(Runnable action)`
* side effects on `Some`
    * `Option<T> peek(Consumer<? super T> action)`
* lazy alternative (in `Optional` since 11)
    * `Option<T> orElse(Supplier<? extends Option<? extends T>> supplier)`
* `transform` function could be nearly always replaced with more expressive and natural `map(...).orElse(...)`
    ```
    given:
    Function<Option<Integer>, String> transformer = { it.isEmpty() ? '' : it.get().toString() }
    expect:
    Option.of(5).transform(transformer) == '5'
    ```
    same as
    ```
    given:
    Function<Option<Integer>, String> transformer = { it.isEmpty() ? '' : it.get().toString() }
    expect:
    transformerFive = Option.of(5)
        .map { it.toString() }
        .orElse('') == '5'
    ```
* conversion `Option<T>` <-> `List<T>`
    * `option.toList()`
    * `list.toOption()`
* could be treated as collection
    * `boolean contains(T element)`
    * `boolean exists(Predicate<? super T> predicate)`
    * `boolean forAll(Predicate<? super T> predicate)`
* well written equals
    * `Some`
        ```
        return (obj == this) || (obj instanceof Some && Objects.equals(value, ((Some<?>) obj).value));
        ```
    * `None`
        ```
        return o == this
        ```
* map does not breaks monadic laws
    ```
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
    ```