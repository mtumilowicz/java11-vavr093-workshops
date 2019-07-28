package com.example.vavr.validation.workshop.person.gateway.input.validation

import com.example.vavr.validation.workshop.person.domain.NewAddressCommand
import com.example.vavr.validation.workshop.person.domain.NewPersonCommand
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest
import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest
import com.example.vavr.validation.workshop.person.gateway.input.validation.NewPersonRequestValidatorAnswer
import com.example.vavr.validation.workshop.person.patterns.Age
import com.example.vavr.validation.workshop.person.patterns.City
import com.example.vavr.validation.workshop.person.patterns.Email
import com.example.vavr.validation.workshop.person.patterns.Emails
import com.example.vavr.validation.workshop.person.patterns.Name
import com.example.vavr.validation.workshop.person.patterns.PostalCode
import io.vavr.collection.List
import io.vavr.control.Validation
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2019-05-12.
 */
class NewPersonRequestValidatorAnswerTest extends Specification {
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
        def validation = NewPersonRequestValidatorAnswer.validate(request)

        then:
        validation == Validation.valid(NewPersonCommand.builder()
                .age(Age.of(16))
                .name(Name.of('a'))
                .emails(new Emails(List.of(Email.of('aaa@aaa.pl'))))
                .address(NewAddressCommand.builder()
                        .city(City.of('Warsaw'))
                        .postalCode(PostalCode.of('00-001'))
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
        def validation = NewPersonRequestValidatorAnswer.validate(request)

        then:
        validation == Validation.invalid(List.of(
                'Name: % is not valid!',
                'Email: a is not valid!',
                'City: * is not valid!, Postal Code: & is not valid!',
                'Age: -1 is not > 0'))
    }
}
