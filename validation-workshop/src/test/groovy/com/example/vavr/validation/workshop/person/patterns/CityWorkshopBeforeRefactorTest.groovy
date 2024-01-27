package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.intrastructure.ValidationException
import io.vavr.collection.List
import spock.lang.Specification

class CityWorkshopBeforeRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        expect:
        City.validateWorkshop('Warsaw') == City.unsafeFrom('Warsaw')
    }

    def "validateWorkshop - invalid"() {
        when:
        City.validateWorkshop('%')

        then:
        ValidationException ex = thrown()
        ex.getErrors() == List.of('City: % is not valid!')
    }
}
