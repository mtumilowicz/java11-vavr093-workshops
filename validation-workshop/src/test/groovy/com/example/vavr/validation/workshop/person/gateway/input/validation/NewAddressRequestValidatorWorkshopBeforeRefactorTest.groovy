package com.example.vavr.validation.workshop.person.gateway.input.validation

import com.example.vavr.validation.workshop.intrastructure.ValidationException
import com.example.vavr.validation.workshop.person.domain.NewAddressCommand
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest
import com.example.vavr.validation.workshop.person.patterns.City
import com.example.vavr.validation.workshop.person.patterns.PostalCode
import io.vavr.collection.List
import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-05-13.
 */
class NewAddressRequestValidatorWorkshopBeforeRefactorTest extends Specification {
    def "test validate - all fields invalid"() {
        given:
        def request = NewAddressRequest.builder()
                .city('*')
                .postalCode('&')
                .build()

        when:
        NewAddressRequestValidatorWorkshop.validate(request)

        then:
        ValidationException ex = thrown()
        ex.errors == List.of('City: * is not valid!', 'Postal Code: & is not valid!')
    }

    def "test validate - all fields valid"() {
        given:
        def request = NewAddressRequest.builder()
                .city('Warsaw')
                .postalCode('00-001')
                .build()

        when:
        def command = NewAddressRequestValidatorWorkshop.validate(request)

        then:
        command == NewAddressCommand.builder()
                .city(City.of('Warsaw'))
                .postalCode(PostalCode.of('00-001'))
                .build()
    }
}
