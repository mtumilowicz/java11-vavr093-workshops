package com.example.vavr.validation.workshop.person.patterns;

import lombok.Value;

@Value(staticConstructor = "of")
public class PersonId {
    Integer raw;
}
