package com.example.vavr.validation.workshop.person.gateway.input.validation

import com.example.vavr.validation.workshop.person.domain.NewAddressCommand
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest
import com.example.vavr.validation.workshop.person.patterns.City
import com.example.vavr.validation.workshop.person.patterns.PostalCode
import io.vavr.collection.List
import io.vavr.control.Validation
import spock.lang.Specification

class NewAddressRequestValidatorWorkshopAfterRefactorTest extends Specification {
    def "test validate - all fields invalid"() {
        given:
        def request = NewAddressRequest.builder()
                .city('*')
                .postalCode('&')
                .build()

        when:
        def validation = NewAddressRequestValidatorWorkshop.validate(request)

        then:
        validation == Validation.invalid(List.of('City: * is not valid!', 'Postal Code: & is not valid!'))
    }

    def "test validate - all fields valid"() {
        given:
        def request = NewAddressRequest.builder()
                .city('Warsaw')
                .postalCode('00-001')
                .build()

        when:
        def validation = NewAddressRequestValidatorWorkshop.validate(request)

        then:
        validation == Validation.valid(NewAddressCommand.builder()
                .city(City.of('Warsaw'))
                .postalCode(PostalCode.of('00-001'))
                .build())
    }
}
