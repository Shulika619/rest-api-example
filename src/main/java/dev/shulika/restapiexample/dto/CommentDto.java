package dev.shulika.restapiexample.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Value
public class CommentDto implements Serializable {

    @NotBlank(message = "Text is mandatory")
    @Length(min = 1, max = 1024, message = "Text must be from 1 to 1024")
    String text;

    @NotBlank(message = "Task id is mandatory")
    @Positive(message = "Task id must be positive Long")
    Long taskId;

}