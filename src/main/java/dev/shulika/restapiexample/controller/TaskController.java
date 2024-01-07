package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.task.*;
import dev.shulika.restapiexample.service.TaskService;
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
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public Page<TaskResponseDto> getAll(Pageable pageable) {
        return taskService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@Valid @PathVariable Long id) {
        TaskResponseDto taskResponseDto = taskService.findById(id);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @GetMapping("/authors/{personId}")
    public Page<TaskResponseDto> getByAuthorId(@Valid @PathVariable Long personId, Pageable pageable) {
        return taskService.findByAuthor(personId, pageable);
    }

    @GetMapping("/authors/{personId}/comments")
    public Page<TaskWithCommentDto> getByAuthorIdWithComments(@Valid @PathVariable Long personId, Pageable pageable) {
        return taskService.findByAuthorWithComments(personId, pageable);
    }

    @GetMapping("/executors/{personId}")
    public Page<TaskResponseDto> getByExecutorId(@Valid @PathVariable Long personId, Pageable pageable) {
        return taskService.findByExecutor(personId, pageable);
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> create(@Valid @RequestBody TaskRequestDto taskRequestDto) {
        TaskResponseDto taskResponseDto = taskService.create(taskRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(
            @Valid @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto taskRequestDto
    ) {
        TaskResponseDto taskResponseDto = taskService.updateById(id, taskRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> updateStatus(
            @Valid @PathVariable Long id,
            @Valid @RequestBody TaskStatusRequestDto taskStatusRequestDto
    ) {
        TaskResponseDto taskResponseDto = taskService.updateStatusById(id, taskStatusRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @PutMapping("/{id}/executor")
    public ResponseEntity<TaskResponseDto> updateExecutor(
            @Valid @PathVariable Long id,
            @Valid @RequestBody TaskExecutorRequestDto taskExecutorRequestDto
    ) {
        TaskResponseDto taskResponseDto = taskService.updateExecutorById(id, taskExecutorRequestDto);
        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @PathVariable Long id) {
        taskService.deleteById(id);
    }

}
