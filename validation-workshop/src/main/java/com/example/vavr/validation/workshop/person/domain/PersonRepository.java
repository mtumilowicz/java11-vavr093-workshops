package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.PersonId;
import org.springframework.stereotype.Repository;

@Repository
public class PersonRepository {
    Person save(Person person) {
        return person.withId(PersonId.of(1));
    }
}
