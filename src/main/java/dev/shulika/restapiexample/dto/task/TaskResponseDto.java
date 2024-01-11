package dev.shulika.restapiexample.dto.task;

import dev.shulika.restapiexample.model.Priority;
import dev.shulika.restapiexample.model.Status;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class TaskResponseDto implements Serializable {

    Long id;

    String title;

    String description;

    Status status;

    Priority priority;

    Long authorId;

    Long executorId;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}