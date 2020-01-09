package com.example.vavr.validation.workshop.person.patterns

import io.vavr.control.Validation
import spock.lang.Specification

class NameWorkshopAfterRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        expect:
        Name.validateWorkshop('Alfred') == Validation.valid(Name.of('Alfred'))
    }

    def "validateWorkshop - invalid"() {
        expect:
        Name.validateWorkshop('%') == Validation.invalid('Name: % is not valid!')
    }

}
