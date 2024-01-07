package dev.shulika.restapiexample.dto.task;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class TaskExecutorRequestDto {

    @NotNull(message = "Executor id is mandatory")
    @Positive(message = "Executor id must be positive Long")
    Long executorId;

}
