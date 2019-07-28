package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.person.patterns.PostalCode
import io.vavr.control.Validation
import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-05-14.
 */
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
