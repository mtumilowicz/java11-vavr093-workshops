package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.*;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Wither;

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
