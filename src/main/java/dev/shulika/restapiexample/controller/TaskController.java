package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.TaskExecutorRequestDto;
import dev.shulika.restapiexample.dto.TaskRequestDto;
import dev.shulika.restapiexample.dto.TaskResponseDto;
import dev.shulika.restapiexample.dto.TaskStatusRequestDto;
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
