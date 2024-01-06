package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.task.TaskExecutorRequestDto;
import dev.shulika.restapiexample.dto.task.TaskRequestDto;
import dev.shulika.restapiexample.dto.task.TaskResponseDto;
import dev.shulika.restapiexample.dto.task.TaskStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskResponseDto> findAll(Pageable pageable);

    TaskResponseDto findById(Long id);

    TaskResponseDto create(TaskRequestDto taskRequestDto);

    TaskResponseDto updateById(Long id, TaskRequestDto taskRequestDto);

    TaskResponseDto updateStatusById(Long id, TaskStatusRequestDto taskStatusRequestDto);

    TaskResponseDto updateExecutorById(Long id, TaskExecutorRequestDto taskExecutorRequestDto);

    void deleteById(Long id);

}
