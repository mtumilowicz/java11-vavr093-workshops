import lombok.Value;

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
