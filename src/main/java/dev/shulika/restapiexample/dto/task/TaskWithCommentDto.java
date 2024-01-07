package dev.shulika.restapiexample.dto.task;

import dev.shulika.restapiexample.model.Priority;
import dev.shulika.restapiexample.model.Status;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class TaskWithCommentDto implements Serializable {

    Long id;
    String title;
    String description;
    Status status;
    Priority priority;
    Long authorId;
    Long executorId;
    List<CommentDto> comments;

    @Value
    public static class CommentDto implements Serializable {
        Long id;
        String text;
    }

}