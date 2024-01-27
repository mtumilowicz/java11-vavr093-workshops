package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.patterns.*
import io.vavr.collection.List
import spock.lang.Specification

class PersonRepositoryTest extends Specification {
    def "test save"() {
        given:
        def person = Person.builder()
                .age(Age.unsafeFrom(14))
                .name(Name.unsafeFrom('a'))
                .emails(new Emails(List.of(Email.unsafeFrom('aaa@aaa.pl'))))
                .address(Address.builder()
                        .city(City.unsafeFrom('Warsaw'))
                        .postalCode(PostalCode.unsafeFrom('00-001'))
                        .build())
                .build()

        expect:
        new PersonRepository().save(person) == person.withId(PersonId.of(1))
    }
}
