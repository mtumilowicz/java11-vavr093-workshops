package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.PersonId;
import org.springframework.stereotype.Repository;

/**
 * Created by mtumilowicz on 2019-05-09.
 */
@Repository
public class PersonRepository {

    Person save(Person person) {
        return person.withId(PersonId.of(1));
    }
}
