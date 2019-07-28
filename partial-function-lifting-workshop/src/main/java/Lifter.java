import io.vavr.PartialFunction;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.function.Function;

/**
 * Created by mtumilowicz on 2019-03-05.
 */
class Lifter {
    static <T, R> Function<T, Option<R>> lift(PartialFunction<T, R> function) {
        return a -> Option.none(); // use Option.when
    }

    static <T, R> Function<T, Option<R>> lift(Function<T, R> function) {
        return a -> Option.none(); // use Try.of, toOption
    }

    static <T, R> Function<T, Try<R>> liftTry(Function<T, R> function) {
        return x -> Try.success(null); // use Try.of
    }
}
