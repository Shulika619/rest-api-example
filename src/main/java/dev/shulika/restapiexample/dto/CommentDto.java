package dev.shulika.restapiexample.dto;

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
public class CommentDto implements Serializable {

    @NotBlank(message = "Text is mandatory")
    @Length(min = 1, max = 1024, message = "Text must be from 1 to 1024")
    String text;

    @NotNull(message = "Task id is mandatory")
    @Positive(message = "Task id must be positive Long")
    Long taskId;

}