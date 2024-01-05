package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.TaskDto;
import dev.shulika.restapiexample.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskDto> findAll(Pageable pageable);

    TaskDto findById(Long id);

    Task create(TaskDto taskDto);

    Task updateById(Long id, TaskDto taskDto);

    void deleteById(Long id);

}
