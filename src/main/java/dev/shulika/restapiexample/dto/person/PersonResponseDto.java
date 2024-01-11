package dev.shulika.restapiexample.dto.person;

import dev.shulika.restapiexample.model.Role;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class PersonResponseDto implements Serializable {

    Long id;

    String firstName;

    String lastName;

    String email;

    Role role;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}