package dev.shulika.restapiexample.dto;

import dev.shulika.restapiexample.model.Priority;
import dev.shulika.restapiexample.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto implements Serializable {

    Long id;

    String title;

    String description;

    Status status;

    Priority priority;

    Long authorId;

    Long executorId;

}