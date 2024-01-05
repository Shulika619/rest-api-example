package dev.shulika.restapiexample.dto;

import dev.shulika.restapiexample.model.Priority;
import dev.shulika.restapiexample.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto implements Serializable {

    @NotBlank(message = "Title is mandatory")
    @Length(min = 3, max = 255, message = "Title must be from 3 to 255")
    String title;

    @NotBlank(message = "Description is mandatory")
    @Length(min = 3, max = 1024, message = "Description must be from 3 to 1024")
    String description;

    @NotNull(message = "Status is mandatory")
    Status status;

    @NotNull(message = "Priority is mandatory")
    Priority priority;

    @NotNull(message = "Author id is mandatory")
    @Positive(message = "Author id must be positive Long")
    Long authorId;

    @NotNull(message = "Executor id is mandatory")
    @Positive(message = "Executor id must be positive Long")
    Long executorId;

}