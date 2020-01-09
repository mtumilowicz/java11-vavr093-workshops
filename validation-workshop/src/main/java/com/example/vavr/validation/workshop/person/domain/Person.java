package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.Age;
import com.example.vavr.validation.workshop.person.patterns.Emails;
import com.example.vavr.validation.workshop.person.patterns.Name;
import com.example.vavr.validation.workshop.person.patterns.PersonId;
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
