package dev.shulika.restapiexample.dto;

import dev.shulika.restapiexample.model.Priority;
import dev.shulika.restapiexample.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Value
public class TaskDto implements Serializable {

    @NotBlank(message = "Title is mandatory")
    @Length(min = 3, max = 255, message = "Title must be from 3 to 255")
    String title;

    @NotBlank(message = "Description is mandatory")
    @Length(min = 3, max = 1024, message = "Description must be from 3 to 1024")
    String description;

    @NotBlank(message = "Status is mandatory")
    @Length(min = 3, max = 20, message = "Status must be from 3 to 20")
    Status status;

    @NotBlank(message = "Priority is mandatory")
    @Length(min = 3, max = 20, message = "Priority must be from 3 to 20")
    Priority priority;

    @NotBlank(message = "Author id is mandatory")
    @Positive(message = "Author id must be positive Long")
    Long authorId;

    @NotBlank(message = "Executor id is mandatory")
    @Positive(message = "Executor id must be positive Long")
    Long executorId;

}