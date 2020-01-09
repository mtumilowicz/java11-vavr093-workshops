package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.Age;
import com.example.vavr.validation.workshop.person.patterns.Emails;
import com.example.vavr.validation.workshop.person.patterns.Name;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class NewPersonCommand {
    Name name;
    NewAddressCommand address;
    Emails emails;
    Age age;
}
