package person;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Address {
    String city;
    String country;
}
