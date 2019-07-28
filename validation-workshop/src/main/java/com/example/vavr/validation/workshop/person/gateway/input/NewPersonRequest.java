package com.example.vavr.validation.workshop.person.gateway.input;

import io.vavr.collection.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mtumilowicz on 2018-12-09.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPersonRequest {
    String name;
    NewAddressRequest address;
    List<String> emails;
    int age;
}

