package dev.shulika.restapiexample.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Value
public class CommentRequestDto implements Serializable {

    @NotBlank(message = "Text is mandatory")
    @Length(min = 1, max = 1024, message = "Text must be from 1 to 1024")
    String text;

    @NotNull(message = "Author id is mandatory")
    @Positive(message = "Author id must be positive Long")
    Long authorId;

    @NotNull(message = "Task id is mandatory")
    @Positive(message = "Task id must be positive Long")
    Long taskId;

}