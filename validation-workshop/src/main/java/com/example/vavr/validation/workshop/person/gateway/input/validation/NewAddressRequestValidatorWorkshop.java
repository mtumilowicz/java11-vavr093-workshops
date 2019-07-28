package com.example.vavr.validation.workshop.person.gateway.input.validation;

import com.example.vavr.validation.workshop.intrastructure.ValidationException;
import com.example.vavr.validation.workshop.person.domain.NewAddressCommand;
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest;
import com.example.vavr.validation.workshop.person.patterns.City;
import com.example.vavr.validation.workshop.person.patterns.PostalCode;
import io.vavr.collection.List;

import java.util.LinkedList;

/**
 * Created by mtumilowicz on 2018-12-09.
 */
class NewAddressRequestValidatorWorkshop {
    
    /**
     * should return Validation<Seq<String>, NewAddressCommand>
     * 
     * hints: 
     *  return Validation
     *      .combine(City.validateWorkshop(request.getCity()), ...)
     *      .ap((city, ...) -> NewAddressCommand.builder()...)
     */
    static NewAddressCommand validate(NewAddressRequest request) {

        var errors = new LinkedList<String>();
        City city = null;
        PostalCode postalCode = null;

        try {
            city = City.validateWorkshop(request.getCity());
        } catch (ValidationException ex) {
            errors.addAll(ex.getErrors().asJava());
        }

        try {
            postalCode = PostalCode.validateWorkshop(request.getPostalCode());
        } catch (ValidationException ex) {
            errors.addAll(ex.getErrors().asJava());
        }

        if (!errors.isEmpty()) {
            throw ValidationException.of(List.ofAll(errors));
        }

        return NewAddressCommand.builder()
                .city(city)
                .postalCode(postalCode)
                .build();
    }
}
