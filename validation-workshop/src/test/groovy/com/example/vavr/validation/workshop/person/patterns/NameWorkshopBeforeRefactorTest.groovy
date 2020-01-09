package com.example.vavr.validation.workshop.person.patterns

import com.example.vavr.validation.workshop.intrastructure.ValidationException
import io.vavr.collection.List
import spock.lang.Specification 

class NameWorkshopBeforeRefactorTest extends Specification {

    def "validateWorkshop - valid"() {
        expect:
        Name.validateWorkshop('Alfred') == Name.of('Alfred')
    }

    def "validateWorkshop - invalid"() {
        when:
        Name.validateWorkshop('%')

        then:
        ValidationException ex = thrown()
        ex.getErrors() == List.of('Name: % is not valid!')
    }
    
}
