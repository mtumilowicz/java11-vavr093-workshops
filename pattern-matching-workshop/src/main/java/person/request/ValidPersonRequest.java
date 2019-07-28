package person.request;

import lombok.Builder;
import lombok.Value;
import person.PersonType;
import person.Salary;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
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
