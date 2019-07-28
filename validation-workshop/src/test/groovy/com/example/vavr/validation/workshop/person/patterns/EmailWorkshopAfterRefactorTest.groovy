package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.person.patterns.Email
import com.example.vavr.validation.workshop.person.patterns.Emails
import io.vavr.collection.List
import io.vavr.control.Validation
import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-05-13.
 */
class EmailWorkshopAfterRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
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
