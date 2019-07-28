package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.person.patterns.City
import io.vavr.control.Validation
import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-05-13.
 */
class CityWorkshopAfterRefactorTest extends Specification {
    
    def "validateWorkshop - valid"() {
        expect:
        City.validateWorkshop('Warsaw') == Validation.valid(City.of('Warsaw'))
    }

    def "validateWorkshop - invalid"() {
        expect:
        City.validateWorkshop('%') == Validation.invalid('City: % is not valid!')
    }
    
}
