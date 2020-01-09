package com.example.vavr.validation.workshop.person.patterns;

import io.vavr.collection.List;
import lombok.NonNull;
import lombok.Value;

@Value
public class Emails {
    List<Email> raw;

    public Emails(@NonNull List<Email> emails) {
        this.raw = emails;
    }
}
