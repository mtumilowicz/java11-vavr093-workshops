import com.google.common.collect.Range;
import io.vavr.PartialFunction;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Random;

/**
 * Created by mtumilowicz on 2019-03-05.
 */
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class RandomIdentity implements PartialFunction<Integer, Integer> {

    Random random = new Random();
    Range<Integer> range;

    @Override
    public Integer apply(Integer o) {
        //if defined: x -> x, otherwise random
        return null;
    }

    @Override
    public boolean isDefinedAt(Integer value) {
        // defined only on 0..3
        return false;
    }
}