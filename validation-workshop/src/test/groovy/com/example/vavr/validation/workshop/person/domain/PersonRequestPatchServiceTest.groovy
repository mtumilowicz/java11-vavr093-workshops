package com.example.vavr.validation.workshop.person.domain

import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest
import io.vavr.control.Option
import spock.lang.Specification

/**
 * Created by mtumilowicz on 2019-05-12.
 */
class PersonRequestPatchServiceTest extends Specification {
    def "test patchSaveRequest - always Option.none"() {
        expect:
        new PersonRequestPatchService().patchSaveRequest(NewPersonRequest.builder()
        .build()) == Option.none()
    }
}
