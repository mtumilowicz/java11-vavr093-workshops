package com.example.vavr.validation.workshop.person.gateway.output;

import com.example.vavr.validation.workshop.person.patterns.PersonId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
