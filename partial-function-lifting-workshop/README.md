# partial-function-lifting-workshop

# project description
* https://www.vavr.io/vavr-docs/#_lifting
* https://static.javadoc.io/io.vavr/vavr/0.9.3/io/vavr/PartialFunction.html
* https://github.com/mtumilowicz/java11-vavr-function-lifting
* in the workshop we will try to fix failing tests `test/groovy/Workshop`
* answers: `test/groovy/Answers` (same tests as in `Workshop` but correctly solved)

# theory in a nutshell
* a partial function from `X` to `Y` is a function `f: K → Y`, 
  for some `K c X`. For `x e X\K` function is undefined
* it generalizes the concept of a function `f: X → Y` by not forcing `f` to map every element of `X` to an element 
    of `Y`
    * that means a partial function works properly only for some input values
    * in programming, if the function is called with a disallowed input value, it will typically throw an exception
        * in particular - every function that throws an exception is a partial function
* partial function (to set intuition)
    ```
    int do(int positive) {
        if (positive < 0) {
                throw new IllegalArgumentException("Only positive integers are allowed"); 
        }
        // other stuff
    }
    ```
* vavr's partial function interface
    ```
    public interface PartialFunction<T, R> {
        R apply(T t);
    
        boolean isDefinedAt(T value); // tests if a value is contained in the function's domain
    }
    ```
    * the caller is responsible for calling the method `isDefinedAt()` before this function is applied to the value
    * if the function is not defined for a specific value, `apply()` may produce an arbitrary result
        * in particular - random values
        * more specifically - it is not guaranteed that the function will throw an exception
    * `do(int positive)` mentioned above - rewritten with vavr:
        ```
        class RandomIdentity implements PartialFunction<Integer, Integer> {
            
            @Override
            public Integer apply(Integer o) {
                if (!isDefinedAt(o)) {
                    throw new IllegalArgumentException("Only positive integers are allowed");
                }
                // other stuff
            }
        
            @Override
            public boolean isDefinedAt(Integer value) {
                return nonNull(value) && value > 0;
            }
        }
        ```
* In programming - we usually **lift** partial function `f: (K c X) -> Y` to `g: X -> Option<Y>` in such a manner:
    * a lifted function returns `Some`, if the function is invoked with allowed input values
        * `g(x).get() = f(x)` on `K`
    * a lifted function returns `None` instead of throwing an exception, if the function is invoked with disallowed
    input values
        * `g(x) = Option.none()` for `x e X\K`
# conclusions in a nutshell
In vavr we have two approaches to lifting:
* lifting function with `Option`
    ```
    Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
    
    Function2<Integer, Integer, Option<Integer>> lifted = Function2.lift(divide);
    
    lifted.apply(1, 0) == Option.none()
    lifted.apply(4, 2) == Option.some(2)
    ```
* lifting function with `Try`
    ```
    Function2<Integer, Integer, Integer> divide = (a, b) -> a / b;
    
    Function2<Integer, Integer, Try<Integer>> lifted = Function2.liftTry(divide);
    
    lifted.apply(1, 0).failure
    lifted.apply(1, 0).cause.class == ArithmeticException
    lifted.apply(4, 2) == Try.success(2)
    ```
* `Try` is nearly always better, because it contains specific exception, which is often a very valuable information,
we do not want to lose