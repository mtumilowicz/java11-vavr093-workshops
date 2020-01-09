package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.intrastructure.ValidationException
import io.vavr.collection.List
import spock.lang.Specification 

class EmailWorkshopBeforeRefactorTest extends Specification {

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
        Email.validateWorkshop(stringEmails) == new Emails(emails)
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

        when:
        Email.validateWorkshop(stringEmails)

        then:
        ValidationException ex = thrown()
        ex.getErrors() == errors
    }

}
