import io.vavr.control.Either;

import java.util.function.Function;

public class BiMapperAnswer {
    static <L, R, LL, RR> Either<LL, RR> bimap(Either<L, R> either, Function<L, LL> lmap, Function<R, RR> rmap) {
        return either.map(rmap).swap().map(lmap).swap();
    }
}
