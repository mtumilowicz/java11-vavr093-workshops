package com.example.vavr.validation.workshop.intrastructure;

import io.vavr.collection.Seq;
import lombok.EqualsAndHashCode;
import lombok.Value;

// class should be removed during PersonControllerWorkshop refactoring
@Value(staticConstructor = "of")
@EqualsAndHashCode(callSuper=false)
public class ValidationException extends RuntimeException {
    Seq<String> errors;
}
