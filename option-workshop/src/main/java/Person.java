import lombok.Value;

/**
 * Created by mtumilowicz on 2019-03-03.
 */
@Value
class Person {
    int age;
     
    boolean isAdult() {
        return age >= 18;
    }
}

@Value
class AdditionalData {
    final String data = "additional data";
}
