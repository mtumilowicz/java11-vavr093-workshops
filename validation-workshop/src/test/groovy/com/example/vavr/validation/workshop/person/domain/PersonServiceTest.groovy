package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.patterns.*
import io.vavr.collection.List
import spock.lang.Specification

class PersonServiceTest extends Specification {
    def "test save"() {
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

        def service = new PersonService(new PersonRepository())

        when:
        def personId = service.save(command)

        then:
        personId == PersonId.of(1)
    }
}
