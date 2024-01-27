package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.patterns.*
import io.vavr.collection.List
import spock.lang.Specification

class PersonServiceTest extends Specification {
    def "test save"() {
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

        def service = new PersonService(new PersonRepository())

        when:
        def personId = service.save(command)

        then:
        personId == PersonId.of(1)
    }
}
