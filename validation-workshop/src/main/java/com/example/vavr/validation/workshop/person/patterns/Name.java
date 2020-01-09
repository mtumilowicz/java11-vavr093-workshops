package com.example.vavr.validation.workshop.person.patterns;

import com.example.vavr.validation.workshop.intrastructure.ValidationException;
import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import io.vavr.control.Validation;
import lombok.NonNull;
import lombok.Value;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Value
public class Name {
    private static final Predicate<String> PREDICATE = Pattern.compile("[\\w]+").asMatchPredicate();

    String raw;

    private Name(String name) {
        this.raw = name;
    }

    public static Name of(@NonNull String name) {
        Preconditions.checkArgument(PREDICATE.test(name));

        return new Name(name);
    }

    public static Validation<String, Name> validateAnswer(String name) {
        return PREDICATE.test(name)
                ? Validation.valid(new Name(name))
                : Validation.invalid("Name: " + name + " is not valid!");
    }

    public static Name validateWorkshop(String name) {
        if (!PREDICATE.test(name)) {
            throw ValidationException.of(List.of("Name: " + name + " is not valid!"));

        }
        return new Name(name);
    }
}
