# validation-workshop

# project description
* https://softwaremill.com/javaslang-data-validation/
* https://www.youtube.com/watch?v=wJn4LHD6Y7o
* https://www.vavr.io/vavr-docs/#_validation  
* https://github.com/mtumilowicz/java11-vavr-validation
* https://github.com/mtumilowicz/java11-jsr303-custom-validation
* in the workshop we will try to rewrite methods in the classes with naming convention: 
`com.example.vavr.validation.workshop.*.*Workshop` 
using hints and refactoring plan depicted below
* answers: `com.example.vavr.validation.workshop.*.*Answer`

## refactoring plan:
1. change in `build.gradle`
   ```
   test {
       exclude '**/**AfterRefactor*'
   }
   ```
   to
   ```
   test {
       exclude '**/**BeforeRefactor*'
   }
   ```
   and we want to fix all failing tests
1. rewrite `patterns` using `Validation` instead of throwing exception
    * method: `validateWorkshop` in classes
        * `Age`
        * `City`
        * `Email`
        * `Name`
        * `PostalCode`
1. rewrite `*Workshop` classes using `Validation` instead  of throwing exception and try-catch blocks 
(more hints in the classes)
    * `NewAddressRequestValidatorWorkshop`
    * `NewPersonRequestValidatorWorkshop`
    * `PersonControllerWorkshop`
1. delete
    * `ErrorMessagesControllerAdvice`
    * `ValidationException`
1. all tests `**/*WorkshopAfterRefactor*` should pass in this step

# theory in a nutshell
* `Validation` is an applicative functor and facilitates accumulating errors
* when trying to compose Monads, the combination process will short circuit at the first encountered error
    as getting subsequent wrapped values depends on results of previous calculations
* `Validation` will continue processing and accumulate all errors (if any)
* it is especially useful when doing validation of multiple fields, say a web form, and you want to know 
all errors encountered, instead of one at a time
* contrary to Bean Validation standard (JSR-303 and JSR-349):
    * https://github.com/mtumilowicz/java11-jsr303-custom-validation
    * https://github.com/mtumilowicz/java-bean-validation2
    * we do not need any infrastructure providers (AOP, dynamic proxies, DI)
    * bad request is not rejected and can be easily handled in a reasonable way (for example
    send to patch service)
* `interface Validation<E, T> extends Value<T>, Serializable`
    * `interface Value<T> extends Iterable<T>`
* two implementations:
    * `final class Valid<E, T> implements Validation<E, T>` - contains valid data
    * `final class Invalid<E, T> implements Validation<E, T>` - is an error representation

# conclusions in a nutshell
* creating
    * `static <E, T> Validation<E, T> valid(T value)`
    * `static <E, T> Validation<E, T> invalid(E error)`
    * example
        ```
        private static final IntPredicate PREDICATE = i -> i > 0;
        
        public static Validation<String, Age> validateAnswer(int age) {
            return PREDICATE.test(age)
                    ? Validation.valid(new Age(age))
                    : Validation.invalid("Age: " + age + " is not > 0");
        }
        ```
* combining
    * `static <E, T1, ...> Builder<E, T1, ...> 
        combine(Validation<E, T1> validation1, Validation<E, T2> validation2, ...)`
    * `public <R> Validation<Seq<E>, R> ap(FunctionN<T1, T2, ..., R> f)`
    * example
        ```
        static Validation<Seq<String>, NewAddressCommand> validate(NewAddressRequest request) {
        
            return Validation
                    .combine(
                            City.validateAnswer(request.getCity()),
                            PostalCode.validateAnswer(request.getPostalCode()))
                    .ap((city, postalCode) -> NewAddressCommand.builder()
                            .city(city)
                            .postalCode(postalCode)
                            .build());
        }
        ```
    * up to 8 arguments
    * `combine` and `functionN` in `ap` should have the same number of params
    * valid type of `combine` params should be the same as type of arguments of `functionN` 
    in `ap`
    * `combine` params order corresponds to the order of params in the `functionN` in `ap`
    * if all of `Validations` in `combine` are `Valid`, the `ap(f)` method maps all results 
    to a single value using a function `f`
    * if at least one of `Validations` is `Invalid`, then `ap(f)` returns `Invalid` with
    all errors aggregated
    * for example, for 3 arguments `ap` looks like
        * `ap` in `Builder` class (`Builder` is returned by `combine` method)
            ```
            public <R> Validation<Seq<E>, R> ap(Function3<T1, T2, T3, R> f) {
                return v3.ap(v2.ap(v1.ap(Validation.valid(f.curried()))));
            }
            ```
        * `ap` in `Validation` class
        ```
            default <U> Validation<Seq<E>, U> ap(Validation<Seq<E>, ? extends Function<? super T, ? extends U>> validation) {
                Objects.requireNonNull(validation, "validation is null");
                if (isValid()) {
                    if (validation.isValid()) {
                        final Function<? super T, ? extends U> f = validation.get();
                        final U u = f.apply(this.get());
                        return valid(u);
                    } else {
                        final Seq<E> errors = validation.getError();
                        return invalid(errors);
                    }
                } else {
                    if (validation.isValid()) {
                        final E error = this.getError();
                        return invalid(List.of(error));
                    } else {
                        final Seq<E> errors = validation.getError();
                        final E error = this.getError();
                        return invalid(errors.append(error));
                    }
                }
            }
        ```
* consuming
    * pattern matching
        ```
        return Match(validation).of(
                Case($Valid($()), ...),
                Case($Invalid($()), ...)
        );
        ```
    * folding to single value
        ```
        return validation.fold(..., ....)
        ```
        * `U fold(Function<? super E, ? extends U> fInvalid, Function<? super T, ? extends U> fValid)`
* transforming
    * mapping
        ```
        validation.map(...)
            .mapError(...)
        ```
        * `Validation<E, U> map(Function<? super T, ? extends U> f)`
        * `Validation<U, T> mapError(Function<? super E, ? extends U> f)`
    * bimapping
        ```
        validation.bimap(..., ...)
        ```
        * `Validation<E2, T2> bimap(Function<? super E, ? extends E2> errorMapper, Function<? super T, ? extends T2> valueMapper)`
    * `bimap` vs `fold` - `bimap` returns `Validation` and `fold` returns any arbitrary type