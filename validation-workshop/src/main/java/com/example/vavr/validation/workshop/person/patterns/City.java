package com.example.vavr.validation.workshop.person.patterns;

import com.example.vavr.validation.workshop.intrastructure.ValidationException;
import com.google.common.base.Preconditions;
import io.vavr.collection.List;
import io.vavr.control.Validation;
import lombok.NonNull;
import lombok.Value;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by mtumilowicz on 2019-05-11.
 */
@Value
public class City {
    private static final Predicate<String> PREDICATE = Pattern.compile("[\\w]+").asMatchPredicate();

    String raw;

    private City(String city) {
        this.raw = city;
    }

    public static City of(@NonNull String city) {
        Preconditions.checkArgument(PREDICATE.test(city));

        return new City(city);
    }

    public static Validation<String, City> validateAnswer(String city) {
        return PREDICATE.test(city)
                ? Validation.valid(new City(city))
                : Validation.invalid("City: " + city + " is not valid!");
    }

    public static City validateWorkshop(String city) {
        if (!PREDICATE.test(city)) {
            throw ValidationException.of(List.of("City: " + city + " is not valid!"));
        }

        return new City(city);
    }
}
