import io.vavr.PartialFunction

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
