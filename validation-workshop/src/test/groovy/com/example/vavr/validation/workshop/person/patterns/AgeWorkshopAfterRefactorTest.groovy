package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.person.patterns.Age
import io.vavr.control.Validation
import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-05-13.
 */
class AgeWorkshopAfterRefactorTest extends Specification {
    
    def "validateWorkshop - valid"() {
        expect:
        Age.validateWorkshop(15) == Validation.valid(Age.of(15))
    }

    def "validateWorkshop - invalid"() {
        expect:
        Age.validateWorkshop(-5) == Validation.invalid('Age: -5 is not > 0')
    }
    
}
