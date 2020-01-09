package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.City;
import com.example.vavr.validation.workshop.person.patterns.PostalCode;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public
class NewAddressCommand {
    PostalCode postalCode;
    City city;
}
