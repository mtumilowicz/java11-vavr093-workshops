import io.vavr.control.Either;

import java.util.function.Function;

/**
 * Created by mtumilowicz on 2019-04-26.
 */
public class BiMapper {
    static <L, R, LL, RR> Either<LL, RR> bimap(Either<L, R> either, Function<L, LL> lmap, Function<R, RR> rmap) {
        return null; // hint: map, swap
    }
}
