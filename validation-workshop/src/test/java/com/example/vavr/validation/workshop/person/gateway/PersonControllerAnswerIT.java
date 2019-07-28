package com.example.vavr.validation.workshop.person.gateway;

import com.example.vavr.validation.workshop.intrastructure.ErrorMessages;
import com.example.vavr.validation.workshop.person.gateway.input.NewAddressRequest;
import com.example.vavr.validation.workshop.person.gateway.input.NewPersonRequest;
import com.example.vavr.validation.workshop.person.gateway.output.NewPersonResponse;
import com.example.vavr.validation.workshop.person.patterns.PersonId;
import io.vavr.collection.List;
import io.vavr.control.Either;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

/**
 * Created by mtumilowicz on 2019-05-11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerAnswerIT {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void newPerson_validRequest() {
//        given
        var responseType = new ParameterizedTypeReference<Either<ErrorMessages, NewPersonResponse>>() {
        };

        var request = NewPersonRequest.builder()
                .age(16)
                .name("a")
                .emails(List.of("aaa@aaa.pl"))
                .address(NewAddressRequest.builder()
                        .city("Warsaw")
                        .postalCode("00-001")
                        .build())
                .build();
//        when
        var response = restTemplate.exchange(
                createURLWithPort("answer/person/new"),
                HttpMethod.POST,
                new HttpEntity<>(request),
                responseType);
        var body = Objects.requireNonNull(response.getBody());

//        then
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertTrue(body.isRight());
        assertThat(body.get(), is(NewPersonResponse.of(PersonId.of(1))));
    }

    @Test
    public void newPerson_fullInvalidRequest() {
//        given
        var responseType = new ParameterizedTypeReference<Either<ErrorMessages, NewPersonResponse>>() {
        };

        var request = NewPersonRequest.builder()
                .age(-1)
                .name("*")
                .emails(List.of("a"))
                .address(NewAddressRequest.builder()
                        .city("$")
                        .postalCode("*")
                        .build())
                .build();
//        when
        var response = restTemplate.exchange(
                createURLWithPort("answer/person/new"),
                HttpMethod.POST,
                new HttpEntity<>(request),
                responseType);
        var body = Objects.requireNonNull(response.getBody());

//        then
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertTrue(body.isLeft());
        assertThat(body.getLeft().getMessages(), is(List.of(
                "Name: * is not valid!",
                "Email: a is not valid!",
                "City: $ is not valid!, Postal Code: * is not valid!",
                "Age: -1 is not > 0")));
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}