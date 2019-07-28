package com.example.vavr.validation.workshop.intrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import lombok.Value;

import static io.vavr.API.*;
import static io.vavr.Predicates.isNull;

/**
 * Created by mtumilowicz on 2019-05-09.
 */
@Value
public class ErrorMessages {
    Seq<String> messages;

    ErrorMessages(@JsonProperty("messages") Seq<String> messages) {
        this.messages = messages;
    }

    public static ErrorMessages of(Seq<String> messages) {
        return Match(messages).of(
                Case($(isNull()), () -> new ErrorMessages(List.empty())),
                Case($(), () -> new ErrorMessages(messages))
        );
    }
}
