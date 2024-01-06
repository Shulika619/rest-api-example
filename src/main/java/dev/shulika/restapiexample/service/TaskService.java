package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.TaskRequestDto;
import dev.shulika.restapiexample.dto.TaskResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskResponseDto> findAll(Pageable pageable);

    TaskResponseDto findById(Long id);

    TaskResponseDto create(TaskRequestDto taskRequestDto);

    TaskResponseDto updateById(Long id, TaskRequestDto taskRequestDto);

    void deleteById(Long id);

}
