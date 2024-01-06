package dev.shulika.restapiexample.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponseDto implements Serializable {

    Long id;
    String firstName;
    String lastName;
    String email;

}