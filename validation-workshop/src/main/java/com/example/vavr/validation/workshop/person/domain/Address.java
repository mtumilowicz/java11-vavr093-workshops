package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.City;
import com.example.vavr.validation.workshop.person.patterns.PostalCode;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
class Address {
    @NonNull
    PostalCode postalCode;

    @NonNull
    City city;
}
