package com.example.vavr.validation.workshop.person.gateway.input;

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
public class NewAddressRequest {
    String postalCode;
    String city;
}
