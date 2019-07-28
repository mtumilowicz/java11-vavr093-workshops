package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.*;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

/**
 * Created by mtumilowicz on 2019-05-09.
 */
@Value
@Builder
@Wither
class Person {
    PersonId id;
    
    @NonNull
    Name name;
    
    @NonNull
    Address address;
    
    @NonNull
    Emails emails;
    
    @NonNull
    Age age;
}
