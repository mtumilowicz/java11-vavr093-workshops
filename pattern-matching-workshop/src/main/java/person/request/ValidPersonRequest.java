package person.request;

import lombok.Builder;
import lombok.Value;
import person.PersonType;
import person.Salary;

@Value
@Builder
public class ValidPersonRequest {
    PersonType type;
    boolean active;
    int balance;
    Salary salary;
    String city;
    String country;
}
