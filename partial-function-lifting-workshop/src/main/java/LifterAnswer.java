import io.vavr.PartialFunction;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.function.Function;

class LifterAnswer {
    static <T, R> Function<T, Option<R>> lift(PartialFunction<T, R> function) {
        return x -> Option.when(function.isDefinedAt(x), () -> function.apply(x));
    }

    static <T, R> Function<T, Option<R>> lift(Function<T, R> function) {
        return x -> Try.of(() -> function.apply(x)).toOption();
    }    
    
    static <T, R> Function<T, Try<R>> liftTry(Function<T, R> function) {
        return x -> Try.of(() -> function.apply(x));
    }
}
