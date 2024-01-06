package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.TaskDto;
import dev.shulika.restapiexample.model.Task;
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
    public Page<TaskDto> getAll(Pageable pageable) {
        return taskService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getById(@Valid @PathVariable Long id) {
        TaskDto taskDto = taskService.findById(id);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.create(taskDto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@Valid @PathVariable Long id, @Valid @RequestBody TaskDto taskDto) {
        Task task = taskService.updateById(id, taskDto);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @PathVariable Long id) {
        taskService.deleteById(id);
    }

}
