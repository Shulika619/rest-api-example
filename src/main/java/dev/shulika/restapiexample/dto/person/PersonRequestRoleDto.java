package dev.shulika.restapiexample.dto.person;

import dev.shulika.restapiexample.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonRequestRoleDto implements Serializable {

    @NotNull(message = "Role is mandatory")
    Role role;

}