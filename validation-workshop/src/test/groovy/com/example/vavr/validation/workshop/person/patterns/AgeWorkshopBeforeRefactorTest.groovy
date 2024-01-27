package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.intrastructure.ValidationException
import io.vavr.collection.List
import spock.lang.Specification

class AgeWorkshopBeforeRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        expect:
        Age.validateWorkshop(15) == Age.unsafeFrom(15)
    }

    def "validateWorkshop - invalid"() {
        when:
        Age.validateWorkshop(-5)

        then:
        ValidationException ex = thrown()
        ex.getErrors() == List.of('Age: -5 is not > 0')
    }
}
