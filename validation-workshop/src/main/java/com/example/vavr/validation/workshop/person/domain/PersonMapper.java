package com.example.vavr.validation.workshop.person.domain;

import lombok.NonNull;

class PersonMapper {
    static Person mapFrom(@NonNull NewPersonCommand command) {
        return Person.builder()
                .name(command.getName())
                .age(command.getAge())
                .address(AddressMapper.mapFrom(command.getAddress()))
                .emails(command.getEmails())
                .build();
    }
}
