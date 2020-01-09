import io.vavr.control.Try;

class ParserAnswer {
    static Try<Integer> parse(String number) {
        return Try.of(() -> Parser.parse(number));
    }
}
