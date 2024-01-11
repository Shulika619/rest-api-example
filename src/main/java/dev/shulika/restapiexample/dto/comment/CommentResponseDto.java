package dev.shulika.restapiexample.dto.comment;

import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class CommentResponseDto implements Serializable {

    Long id;

    String text;

    Long taskId;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}