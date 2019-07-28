package person.request;

import lombok.Builder;
import lombok.Value;
import person.PersonType;

@Value
@Builder
public class PersonRequest {
    PersonType type;
    boolean active;
    int balance;
    int salary;
    String city;
    String country;
}

