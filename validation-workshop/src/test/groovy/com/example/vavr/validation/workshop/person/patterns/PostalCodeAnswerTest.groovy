package com.example.vavr.validation.workshop.person.patterns


import io.vavr.control.Validation
import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-05-14.
 */
class PostalCodeAnswerTest extends Specification {

    def "validateAnswer - valid"() {
        expect:
        PostalCode.validateAnswer('00-001') == Validation.valid(PostalCode.of('00-001'))
    }

    def "validateAnswer - invalid"() {
        expect:
        PostalCode.validateAnswer('%') == Validation.invalid('Postal Code: % is not valid!')
    }
    
}
