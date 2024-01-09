package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.task.*;
import dev.shulika.restapiexample.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
@Tag(name = "Task", description = "Task management APIs")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Get tasks", description = "Provides all tasks, supports pagination and filtering")
    @GetMapping
    public Page<TaskResponseDto> getAll(Pageable pageable) {
        return taskService.findAll(pageable);
    }

    @Operation(summary = "Get a task", description = "Provides task by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@Valid @PathVariable Long id) {
        TaskResponseDto taskResponseDto = taskService.findById(id);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Get a task with comments",
            description = "Provides task with all its comments by id")
    @GetMapping("/{id}/comments")
    public ResponseEntity<TaskWithCommentDto> getByIdWithComments(@Valid @PathVariable Long id) {
        TaskWithCommentDto taskWithCommentDto = taskService.findByIdWithComments(id);
        return new ResponseEntity<>(taskWithCommentDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Get author tasks",
            description = "Provides all author tasks by id(person), supports pagination and filtering")
    @GetMapping("/authors/{personId}")
    public Page<TaskResponseDto> getByAuthorId(@Valid @PathVariable Long personId, Pageable pageable) {
        return taskService.findByAuthor(personId, pageable);
    }

    @Operation(
            summary = "Get author tasks with comments",
            description = "Provides all author tasks with comments by id(person), supports pagination and filtering")
    @GetMapping("/authors/{personId}/comments")
    public Page<TaskWithCommentDto> getByAuthorIdWithComments(@Valid @PathVariable Long personId, Pageable pageable) {
        return taskService.findByAuthorWithComments(personId, pageable);
    }

    @Operation(
            summary = "Get executor tasks",
            description = "Provides all executor tasks by id(person), supports pagination and filtering")
    @GetMapping("/executors/{personId}")
    public Page<TaskResponseDto> getByExecutorId(@Valid @PathVariable Long personId, Pageable pageable) {
        return taskService.findByExecutor(personId, pageable);
    }

    @Operation(summary = "Create a task", description = "Allows you to create a task")
    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        TaskResponseDto taskResponseDto = taskService.create(taskRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a task", description = "Allows you to update a task by its id")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(
            @Valid @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto taskRequestDto
    ) {
        TaskResponseDto taskResponseDto = taskService.updateById(id, taskRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a task status",
            description = "Allows you to update the status of a task by id")
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateStatus(
            @Valid @PathVariable Long id,
            @Valid @RequestBody TaskStatusRequestDto taskStatusRequestDto
    ) {
        TaskResponseDto taskResponseDto = taskService.updateStatusById(id, taskStatusRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Update a task executor",
            description = "Allows you to update the executor of a task by id")
    @PutMapping("/{id}/executor")
    public ResponseEntity<TaskResponseDto> updateExecutor(
            @Valid @PathVariable Long id,
            @Valid @RequestBody TaskExecutorRequestDto taskExecutorRequestDto
    ) {
        TaskResponseDto taskResponseDto = taskService.updateExecutorById(id, taskExecutorRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete a task", description = "Allows you to delete a task by its id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @PathVariable Long id) {
        taskService.deleteById(id);
    }

}
