import io.vavr.control.Try;

import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.joining;

/**
 * Created by mtumilowicz on 2019-03-03.
 */
public class TWRAnswer extends TWR {
    static Try<String> usingVavr(String path) {
        return Try.withResources(() -> Files.lines(Paths.get(path)))
                .of(stream -> stream.collect(joining(",")));
    }
}
