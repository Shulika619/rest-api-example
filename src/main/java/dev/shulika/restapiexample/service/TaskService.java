package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.TaskExecutorRequestDto;
import dev.shulika.restapiexample.dto.TaskRequestDto;
import dev.shulika.restapiexample.dto.TaskResponseDto;
import dev.shulika.restapiexample.dto.TaskStatusRequestDto;
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
