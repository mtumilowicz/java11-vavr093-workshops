package com.example.vavr.validation.workshop.person.patterns;

import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-09.
 */
@Value(staticConstructor = "of")
public class PersonId {
    Integer raw;
}
