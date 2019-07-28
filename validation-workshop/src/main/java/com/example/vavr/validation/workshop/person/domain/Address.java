package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.City;
import com.example.vavr.validation.workshop.person.patterns.PostalCode;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-10.
 */
@Value
@Builder
class Address {
    @NonNull
    PostalCode postalCode;
    
    @NonNull
    City city;
}
