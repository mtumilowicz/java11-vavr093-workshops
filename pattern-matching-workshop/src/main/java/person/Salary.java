package person;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-02.
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Salary {
    int value;
    
    public static Salary of(int salary) {
        Preconditions.checkArgument(salary >= 0);
        
        return new Salary(salary);
    }
}
