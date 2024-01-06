package dev.shulika.restapiexample.dto;

import dev.shulika.restapiexample.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusRequestDto {

    @NotNull(message = "Status is mandatory")
    Status status;

}
