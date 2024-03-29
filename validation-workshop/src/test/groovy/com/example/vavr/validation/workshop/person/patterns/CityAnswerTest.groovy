package com.example.vavr.validation.workshop.person.patterns

import io.vavr.control.Validation
import spock.lang.Specification

class CityAnswerTest extends Specification {

    def "validateAnswer - valid"() {
        expect:
        City.validateAnswer('Warsaw') == Validation.valid(City.unsafeFrom('Warsaw'))
    }

    def "validateAnswer - invalid"() {
        expect:
        City.validateAnswer('%') == Validation.invalid('City: % is not valid!')
    }

}
