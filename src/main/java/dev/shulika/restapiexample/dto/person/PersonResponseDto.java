package dev.shulika.restapiexample.dto.person;

import lombok.Value;

import java.io.Serializable;

@Value
public class PersonResponseDto implements Serializable {

    Long id;

    String firstName;

    String lastName;

    String email;

}