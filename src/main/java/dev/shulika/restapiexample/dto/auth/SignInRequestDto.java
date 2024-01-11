package dev.shulika.restapiexample.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Incorrect email format")
    @Length(min = 6, max = 55, message = "Email must be from 6 to 55")
    String email;

    @NotBlank(message = "Password is mandatory")
    @Length(min = 6, max = 55, message = "Password must be from 6 to 55")
    String password;

}