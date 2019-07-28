import lombok.Builder;
import lombok.Value;

/**
 * Created by mtumilowicz on 2019-04-26.
 */
@Value
@Builder
class PersonRequest {
    int id;
    int age;
}
