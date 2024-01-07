package dev.shulika.restapiexample.dto.task;

import dev.shulika.restapiexample.model.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class TaskStatusRequestDto {

    @NotNull(message = "Status is mandatory")
    Status status;

}
