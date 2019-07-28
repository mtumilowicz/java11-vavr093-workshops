package person;

import lombok.Builder;
import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-01.
 */
@Value
@Builder
public class Address {
    String city;
    String country;
}
