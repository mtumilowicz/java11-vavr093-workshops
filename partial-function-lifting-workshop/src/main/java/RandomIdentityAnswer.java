import com.google.common.collect.Range;
import io.vavr.PartialFunction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Random;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class RandomIdentityAnswer implements PartialFunction<Integer, Integer> {
    
    Random random = new Random();
    Range<Integer> range;

    @Override
    public Integer apply(Integer o) {
        return isDefinedAt(o) ? o : random.nextInt();
    }

    @Override
    public boolean isDefinedAt(Integer value) {
        return range.contains(value);
    }
}
