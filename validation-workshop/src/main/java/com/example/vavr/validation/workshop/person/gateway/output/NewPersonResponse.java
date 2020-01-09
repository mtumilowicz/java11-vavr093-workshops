package com.example.vavr.validation.workshop.person.gateway.output;

import com.example.vavr.validation.workshop.person.patterns.PersonId;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPersonResponse {
    int id;

    public static NewPersonResponse of(PersonId id) {
        return NewPersonResponse.builder()
                .id(id.getRaw())
                .build();
    }
}
