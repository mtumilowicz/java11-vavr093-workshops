package com.example.vavr.validation.workshop.person.domain;

import com.example.vavr.validation.workshop.person.patterns.PersonId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * Created by mtumilowicz on 2019-05-09.
 */
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PersonService {
    PersonRepository personRepository;

    public PersonId save(NewPersonCommand newPersonCommand) {
        return personRepository.save(PersonMapper.mapFrom(newPersonCommand)).getId();
    }
}
