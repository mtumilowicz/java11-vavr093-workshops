package com.example.vavr.validation.workshop.person.patterns;

import com.example.vavr.validation.workshop.intrastructure.ValidationException;
import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import io.vavr.control.Validation;
import lombok.Value;

import java.util.function.IntPredicate;

@Value
public class Age {
    private static final IntPredicate PREDICATE = i -> i > 0;

    int raw;

    private Age(int age) {
        this.raw = age;
    }

    public static Age unsafeFrom(int age) {
        Preconditions.checkArgument(PREDICATE.test(age));

        return new Age(age);
    }

    public static Validation<String, Age> validateAnswer(int age) {
        return PREDICATE.test(age)
                ? Validation.valid(new Age(age))
                : Validation.invalid("Age: " + age + " is not > 0");
    }

    public static Age validateWorkshop(int age) {
        if (!PREDICATE.test(age)) {
            throw ValidationException.of(List.of("Age: " + age + " is not > 0"));
        }

        return new Age(age);
    }
}
