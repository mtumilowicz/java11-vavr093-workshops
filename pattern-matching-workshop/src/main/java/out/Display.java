package out;

import lombok.Getter;
import lombok.ToString;

/**
 * Created by mtumilowicz on 2019-05-03.
 */
@ToString
public class Display {
    @Getter
    private String message;
    
    public void push(String message) {
        this.message = message;
    }
}
