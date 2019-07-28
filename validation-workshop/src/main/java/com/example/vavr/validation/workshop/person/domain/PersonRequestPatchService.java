package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;

/**
 * Created by mtumilowicz on 2019-05-09.
 */
@Service
public class PersonRequestPatchService {
    public Option<NewPersonCommand> patchSaveRequest(NewPersonRequest request) {
        return Option.none();
    }
}
