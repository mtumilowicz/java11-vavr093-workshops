package out;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Display {
    @Getter
    private String message;
    
    public void push(String message) {
        this.message = message;
    }
}
