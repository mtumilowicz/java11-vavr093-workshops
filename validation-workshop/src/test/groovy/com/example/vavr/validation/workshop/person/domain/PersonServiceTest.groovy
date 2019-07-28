package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.patterns.Age
import com.example.vavr.validation.workshop.person.patterns.City
import com.example.vavr.validation.workshop.person.patterns.Email
import com.example.vavr.validation.workshop.person.patterns.Emails
import com.example.vavr.validation.workshop.person.patterns.Name
import com.example.vavr.validation.workshop.person.patterns.PersonId
import com.example.vavr.validation.workshop.person.patterns.PostalCode
import io.vavr.collection.List
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2019-05-12.
 */
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
