import lombok.Builder;
import lombok.Value;

@Value
@Builder
class PersonRequest {
    int id;
    int age;
}
