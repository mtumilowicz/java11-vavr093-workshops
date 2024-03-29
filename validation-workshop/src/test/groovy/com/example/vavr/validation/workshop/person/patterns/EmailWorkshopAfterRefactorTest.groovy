package com.example.vavr.validation.workshop.person.patterns

import io.vavr.collection.List
import io.vavr.control.Validation
import spock.lang.Specification

class EmailWorkshopAfterRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        given:
        def stringEmails = List.of(
                'aaa@aaa.pl',
                'bbb@bbb.pl'
        )
        def emails = List.of(
                Email.unsafeFrom('aaa@aaa.pl'),
                Email.unsafeFrom('bbb@bbb.pl')
        )

        expect:
        Email.validateWorkshop(stringEmails) == Validation.valid(new Emails(emails))
    }

    def "validateWorkshop - invalid"() {
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
        Email.validateWorkshop(stringEmails) == Validation.invalid(errors)
    }

}
