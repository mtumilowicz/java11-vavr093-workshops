import io.vavr.control.Try;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.joining;

class TWR {
    static String usingJava(String path) throws IOException {
        String fileLines;
        
        try (var stream = Files.lines(Paths.get(path))) {
            fileLines = stream.collect(joining(","));
        }

        return fileLines;
    }
    
    // rewrite usingJava with vavr try-with-resources
    static Try<String> usingVavr(String path) {
        return null;
    }
}
