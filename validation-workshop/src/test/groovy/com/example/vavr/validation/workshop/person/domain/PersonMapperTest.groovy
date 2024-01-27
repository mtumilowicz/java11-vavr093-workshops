package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.patterns.*
import io.vavr.collection.List
import spock.lang.Specification

class PersonMapperTest extends Specification {
    def "map command with all fields"() {
        given:
        def command = NewPersonCommand.builder()
                .age(Age.unsafeFrom(14))
                .name(Name.unsafeFrom('a'))
                .emails(new Emails(List.of(Email.unsafeFrom('aaa@aaa.pl'))))
                .address(NewAddressCommand.builder()
                        .city(City.unsafeFrom('Warsaw'))
                        .postalCode(PostalCode.unsafeFrom('00-001'))
                        .build())
                .build()

        when:
        def person = PersonMapper.mapFrom(command)

        then:
        person == Person.builder()
                .age(Age.unsafeFrom(14))
                .name(Name.unsafeFrom('a'))
                .emails(new Emails(List.of(Email.unsafeFrom('aaa@aaa.pl'))))
                .address(Address.builder()
                        .city(City.unsafeFrom('Warsaw'))
                        .postalCode(PostalCode.unsafeFrom('00-001'))
                        .build())
                .build()
    }
}
