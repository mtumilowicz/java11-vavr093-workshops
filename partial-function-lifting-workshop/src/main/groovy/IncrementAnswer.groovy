import io.vavr.PartialFunction

/**
 * Created by mtumilowicz on 2019-03-04.
 */
class IncrementAnswer implements PartialFunction<Integer, Integer> {

    final IntRange range

    IncrementAnswer(IntRange range) {
        this.range = range
    }

    @Override
    Integer apply(Integer o) {
        return isDefinedAt(o) ? ++o : -1
    }

    @Override
    boolean isDefinedAt(Integer value) {
        return range.contains(value)
    }
}
