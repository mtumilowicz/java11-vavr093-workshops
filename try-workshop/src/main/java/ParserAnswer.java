import io.vavr.control.Try;

/**
 * Created by mtumilowicz on 2019-04-05.
 */
class ParserAnswer {
    static Try<Integer> parse(String number) {
        return Try.of(() -> Parser.parse(number));
    }
}
