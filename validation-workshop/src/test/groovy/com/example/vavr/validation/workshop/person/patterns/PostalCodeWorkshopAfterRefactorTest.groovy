package com.example.vavr.validation.workshop.person.patterns

import io.vavr.control.Validation
import spock.lang.Specification

class PostalCodeWorkshopAfterRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        expect:
        PostalCode.validateWorkshop('00-001') == Validation.valid(PostalCode.of('00-001'))
    }

    def "validateWorkshop - invalid"() {
        expect:
        PostalCode.validateWorkshop('%') == Validation.invalid('Postal Code: % is not valid!')
    }

}
