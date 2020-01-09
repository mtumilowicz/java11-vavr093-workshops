package com.example.vavr.validation.workshop.person.gateway

import com.example.vavr.validation.workshop.intrastructure.ErrorMessages
import com.example.vavr.validation.workshop.person.domain.PersonRepository
import com.example.vavr.validation.workshop.person.domain.PersonRequestPatchService
import com.example.vavr.validation.workshop.person.domain.PersonService
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest
import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest
import com.example.vavr.validation.workshop.person.gateway.output.NewPersonResponse
import com.example.vavr.validation.workshop.person.patterns.PersonId
import io.vavr.collection.List
import io.vavr.control.Either
import spock.lang.Specification

class PersonControllerAnswerTest extends Specification {

    def "test newPerson - valid request"() {
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

        def controller = new PersonControllerAnswer(
                new PersonService(new PersonRepository()),
                new PersonRequestPatchService())

        when:
        def personId = controller.newPerson(request)

        then:
        personId.getBody() == Either.right(NewPersonResponse.of(PersonId.of(1)))
    }

    def "test newPerson - full invalid request"() {
        given:
        def request = NewPersonRequest.builder()
                .age(-1)
                .name('*')
                .emails(List.of('a'))
                .address(NewAddressRequest.builder()
                        .city('$')
                        .postalCode('*')
                        .build())
                .build()

        def controller = new PersonControllerAnswer(
                new PersonService(new PersonRepository()),
                new PersonRequestPatchService())

        when:
        def personId = controller.newPerson(request)

        then:
        personId.getBody() == Either.left(ErrorMessages.of(List.of(
                'Name: * is not valid!',
                'Email: a is not valid!',
                'City: $ is not valid!, Postal Code: * is not valid!',
                'Age: -1 is not > 0')))
    }
}
