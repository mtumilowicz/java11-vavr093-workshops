package com.example.vavr.validation.workshop.person.gateway.input.validation

import com.example.vavr.validation.workshop.person.domain.NewAddressCommand
import com.example.vavr.validation.workshop.person.domain.NewPersonCommand
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest
import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest
import com.example.vavr.validation.workshop.person.patterns.*
import io.vavr.collection.List
import io.vavr.control.Validation
import spock.lang.Specification

class NewPersonRequestValidatorWorkshopAfterRefactorTest extends Specification {
    def "test validate - all fields valid"() {
        given:
        def request = NewPersonRequest.builder()
                .age(16)
                .name('a')
                .emails(List.of('aaa@aaa.pl'))
                .address(NewAddressRequest.builder()
                        .city('Warsaw')
                        .postalCode('00-001')
                        .build())
                .build()

        when:
        def validation = NewPersonRequestValidatorWorkshop.validate(request)

        then:
        validation == Validation.valid(NewPersonCommand.builder()
                .age(Age.unsafeFrom(16))
                .name(Name.unsafeFrom('a'))
                .emails(new Emails(List.of(Email.unsafeFrom('aaa@aaa.pl'))))
                .address(NewAddressCommand.builder()
                        .city(City.unsafeFrom('Warsaw'))
                        .postalCode(PostalCode.unsafeFrom('00-001'))
                        .build())
                .build())
    }

    def "test validate - all fields invalid"() {
        given:
        def request = NewPersonRequest.builder()
                .age(-1)
                .name('%')
                .emails(List.of('a'))
                .address(NewAddressRequest.builder()
                        .city('*')
                        .postalCode('&')
                        .build())
                .build()

        when:
        def validation = NewPersonRequestValidatorWorkshop.validate(request)

        then:
        validation == Validation.invalid(List.of(
                'Name: % is not valid!',
                'Email: a is not valid!',
                'City: * is not valid!, Postal Code: & is not valid!',
                'Age: -1 is not > 0'))
    }
}
