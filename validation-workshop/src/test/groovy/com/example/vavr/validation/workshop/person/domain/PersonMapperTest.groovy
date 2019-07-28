package com.example.vavr.validation.workshop.person.domain


import com.example.vavr.validation.workshop.person.patterns.*
import io.vavr.collection.List
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2019-05-12.
 */
class PersonMapperTest extends Specification {
    def "map command with all fields"() {
        given:
        def command = NewPersonCommand.builder()
                .age(Age.of(14))
                .name(Name.of('a'))
                .emails(new Emails(List.of(Email.of('aaa@aaa.pl'))))
                .address(NewAddressCommand.builder()
                        .city(City.of('Warsaw'))
                        .postalCode(PostalCode.of('00-001'))
                        .build())
                .build()

        when:
        def person = PersonMapper.mapFrom(command)

        then:
        person == Person.builder()
                .age(Age.of(14))
                .name(Name.of('a'))
                .emails(new Emails(List.of(Email.of('aaa@aaa.pl'))))
                .address(Address.builder()
                        .city(City.of('Warsaw'))
                        .postalCode(PostalCode.of('00-001'))
                        .build())
                .build()
    }
}
