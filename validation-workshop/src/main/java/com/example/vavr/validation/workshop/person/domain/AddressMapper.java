package com.example.vavr.validation.workshop.person.domain;

import lombok.NonNull;

class AddressMapper {
    static Address mapFrom(@NonNull NewAddressCommand command) {
        return Address.builder()
                .city(command.getCity())
                .postalCode(command.getPostalCode())
                .build();
    }
}
