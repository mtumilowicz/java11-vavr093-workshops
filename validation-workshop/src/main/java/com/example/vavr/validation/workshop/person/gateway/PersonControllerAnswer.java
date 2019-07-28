package com.example.vavr.validation.workshop.person.gateway;

import com.example.vavr.validation.workshop.intrastructure.ErrorMessages;
import com.example.vavr.validation.workshop.person.domain.NewPersonCommand;
import com.example.vavr.validation.workshop.person.domain.PersonRequestPatchService;
import com.example.vavr.validation.workshop.person.domain.PersonService;
import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest;
import com.example.vavr.validation.workshop.person.gateway.input.validation.NewPersonRequestValidatorAnswer;
import com.example.vavr.validation.workshop.person.gateway.output.NewPersonResponse;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static io.vavr.API.*;
import static io.vavr.Patterns.*;

/**
 * Created by mtumilowicz on 2019-05-08.
 */
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class PersonControllerAnswer {

    PersonService personService;
    PersonRequestPatchService patchService;

    @PostMapping("answer/person/new")
    public ResponseEntity<Either<ErrorMessages, NewPersonResponse>> newPerson(
            @RequestBody NewPersonRequest newPersonRequest) {
        return Match(NewPersonRequestValidatorAnswer.validate(newPersonRequest)).of(
                Case($Valid($()), this::newPersonCommand),
                Case($Invalid($()), errors -> patchNewPersonCommand(newPersonRequest, errors))
        );
    }

    private ResponseEntity<Either<ErrorMessages, NewPersonResponse>> newPersonCommand(
            NewPersonCommand command) {
        return ResponseEntity.ok(Either.right(NewPersonResponse.of(personService.save(command))));
    }

    private ResponseEntity<Either<ErrorMessages, NewPersonResponse>> patchNewPersonCommand(
            NewPersonRequest newPersonRequest,
            Seq<String> errors) {
        return Match(patchService.patchSaveRequest(newPersonRequest)).of(
                Case($Some($()), this::newPersonCommand),
                Case($None(), () -> ResponseEntity.badRequest().body(Either.left(ErrorMessages.of(errors))))
        );
    }
}
