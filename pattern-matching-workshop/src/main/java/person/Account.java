package person;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Account {
    int balance;
    Salary salary;
}