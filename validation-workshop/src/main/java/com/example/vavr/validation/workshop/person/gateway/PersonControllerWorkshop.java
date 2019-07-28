package com.example.vavr.validation.workshop.person.gateway;

import com.example.vavr.validation.workshop.intrastructure.ValidationException;
import com.example.vavr.validation.workshop.person.domain.NewPersonCommand;
import com.example.vavr.validation.workshop.person.domain.PersonRequestPatchService;
import com.example.vavr.validation.workshop.person.domain.PersonService;
import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest;
import com.example.vavr.validation.workshop.person.gateway.input.validation.NewPersonRequestValidatorWorkshop;
import com.example.vavr.validation.workshop.person.gateway.output.NewPersonResponse;
import io.vavr.collection.Seq;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mtumilowicz on 2019-05-08.
 */
@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class PersonControllerWorkshop {

    PersonService personService;
    PersonRequestPatchService patchService;

    /**
     * rewrite using pattern matching
     * <p>
     * hints - useful methods:
     *  Match(validation).of
     *      Case($Valid($()), ...)
     *      Case($Invalid($()), ...)
     * <p>
     * method should return ResponseEntity<Either<ErrorMessages, NewPersonResponse>>
     */
    @PostMapping("workshop/person/new")
    public ResponseEntity<NewPersonResponse> newPerson(
            @RequestBody NewPersonRequest newPersonRequest) {
        try {
            NewPersonCommand validation = NewPersonRequestValidatorWorkshop.validate(newPersonRequest);
            return newPersonCommand(validation);
        } catch (ValidationException ex) {
            return patchNewPersonCommand(newPersonRequest, ex.getErrors());
        }
    }

    /**
     * method should return ResponseEntity<Either<ErrorMessages, NewPersonResponse>>
     */
    private ResponseEntity<NewPersonResponse> newPersonCommand(
            NewPersonCommand command) {
        return ResponseEntity.ok(NewPersonResponse.of(personService.save(command)));
    }

    /**
     * rewrite using pattern matching
     * <p>
     * hints - useful methods:
     *  Match(validation).of
     *      Case($Some($()), ...)
     *      Case($None(), ...)
     * <p>
     * method should return ResponseEntity<Either<ErrorMessages, NewPersonResponse>>
     * exception should not be thrown at all
     */
    private ResponseEntity<NewPersonResponse> patchNewPersonCommand(
            NewPersonRequest newPersonRequest,
            Seq<String> errors) {

        return patchService.patchSaveRequest(newPersonRequest)
                .map(this::newPersonCommand)
                .getOrElseThrow(() -> ValidationException.of(errors));
    }
}
