package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.intrastructure.ValidationException
import io.vavr.collection.List
import spock.lang.Specification 
/**
 * Created by mtumilowicz on 2019-05-14.
 */
class PostalCodeWorkshopBeforeRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        expect:
        PostalCode.validateWorkshop('00-001') == PostalCode.of('00-001')
    }

    def "validateWorkshop - invalid"() {
        when:
        PostalCode.validateWorkshop('%')

        then:
        ValidationException ex = thrown()
        ex.getErrors() == List.of('Postal Code: % is not valid!')
    }
    
}
