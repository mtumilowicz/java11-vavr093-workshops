package com.example.vavr.validation.workshop.person.patterns


import io.vavr.control.Validation
import spock.lang.Specification

class AgeWorkshopAfterRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        expect:
        Age.validateWorkshop(15) == Validation.valid(Age.unsafeFrom(15))
    }

    def "validateWorkshop - invalid"() {
        expect:
        Age.validateWorkshop(-5) == Validation.invalid('Age: -5 is not > 0')
    }

}
