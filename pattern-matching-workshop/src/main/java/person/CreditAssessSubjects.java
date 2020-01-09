package person;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreditAssessSubjects {
    int balance;
    Salary salary;
    String country;
}
