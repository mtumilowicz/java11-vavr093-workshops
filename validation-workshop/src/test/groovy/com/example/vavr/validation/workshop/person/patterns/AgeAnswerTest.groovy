package com.example.vavr.validation.workshop.person.patterns

import io.vavr.control.Validation
import spock.lang.Specification 

class AgeAnswerTest extends Specification {
    
    def "validateAnswer - valid"() {
        expect:
        Age.validateAnswer(15) == Validation.valid(Age.of(15))
    }

    def "validateAnswer - invalid"() {
        expect:
        Age.validateAnswer(-5) == Validation.invalid('Age: -5 is not > 0')
    }
    
}
