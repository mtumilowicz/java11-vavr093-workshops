package com.example.vavr.validation.workshop.person.patterns

import io.vavr.collection.List
import io.vavr.control.Validation
import spock.lang.Specification

class EmailAnswerTest extends Specification {

    def "validateAnswer - valid"() {
        given:
        def stringEmails = List.of(
                'aaa@aaa.pl',
                'bbb@bbb.pl'
        )
        def emails = List.of(
                Email.of('aaa@aaa.pl'),
                Email.of('bbb@bbb.pl')
        )

        expect:
        Email.validateAnswer(stringEmails) == Validation.valid(new Emails(emails))
    }

    def "validateAnswer - invalid"() {
        given:
        def stringEmails = List.of(
                'a',
                'b'
        )
        def errors = List.of(
                'Email: a is not valid!',
                'Email: b is not valid!'
        )

        expect:
        Email.validateAnswer(stringEmails) == Validation.invalid(errors)
    }

}
