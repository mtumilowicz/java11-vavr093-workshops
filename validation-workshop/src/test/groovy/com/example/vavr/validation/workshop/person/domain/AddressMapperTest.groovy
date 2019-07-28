package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.patterns.City
import com.example.vavr.validation.workshop.person.patterns.PostalCode
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2019-05-12.
 */
class AddressMapperTest extends Specification {
    def "map command with all fields"() {
        given:
        def command = NewAddressCommand.builder()
                .city(City.of('Warsaw'))
                .postalCode(PostalCode.of('00-001'))
                .build()

        when:
        def address = AddressMapper.mapFrom(command)

        then:
        address == Address.builder()
                .city(City.of('Warsaw'))
                .postalCode(PostalCode.of('00-001'))
                .build()
    }
}
