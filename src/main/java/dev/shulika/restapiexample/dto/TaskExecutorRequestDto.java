package dev.shulika.restapiexample.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskExecutorRequestDto {

    @NotNull(message = "Executor id is mandatory")
    @Positive(message = "Executor id must be positive Long")
    Long executorId;

}
