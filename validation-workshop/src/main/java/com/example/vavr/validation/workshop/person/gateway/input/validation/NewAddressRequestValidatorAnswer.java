package com.example.vavr.validation.workshop.person.gateway.input.validation;

import com.example.vavr.validation.workshop.person.domain.NewAddressCommand;
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest;
import com.example.vavr.validation.workshop.person.patterns.City;
import com.example.vavr.validation.workshop.person.patterns.PostalCode;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

class NewAddressRequestValidatorAnswer {
    static Validation<Seq<String>, NewAddressCommand> validate(NewAddressRequest request) {

        return Validation
                .combine(
                        City.validateAnswer(request.getCity()),
                        PostalCode.validateAnswer(request.getPostalCode()))
                .ap((city, postalCode) -> NewAddressCommand.builder()
                        .city(city)
                        .postalCode(postalCode)
                        .build());
    }
}
