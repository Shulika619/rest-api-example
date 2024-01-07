package dev.shulika.restapiexample.dto.comment;

import lombok.Value;

import java.io.Serializable;

@Value
public class CommentResponseDto implements Serializable {

    Long id;

    String text;

    Long taskId;

}