package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.patterns.*
import io.vavr.collection.List
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2019-05-12.
 */
class PersonRepositoryTest extends Specification {
    def "test save"() {
        given:
        def person = Person.builder()
                .age(Age.of(14))
                .name(Name.of('a'))
                .emails(new Emails(List.of(Email.of('aaa@aaa.pl'))))
                .address(Address.builder()
                        .city(City.of('Warsaw'))
                        .postalCode(PostalCode.of('00-001'))
                        .build())
                .build()
        
        expect:
        new PersonRepository().save(person) == person.withId(PersonId.of(1))
    }
}
