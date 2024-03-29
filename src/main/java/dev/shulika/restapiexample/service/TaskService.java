package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.task.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskResponseDto> findAll(Pageable pageable);

    TaskResponseDto findById(Long id);

    TaskWithCommentDto findByIdWithComments(Long id);

    Page<TaskResponseDto> findByAuthor(Long personId, Pageable pageable);

    Page<TaskWithCommentDto> findByAuthorWithComments(Long personId, Pageable pageable);

    Page<TaskResponseDto> findByExecutor(Long personId, Pageable pageable);

    TaskResponseDto create(TaskRequestDto taskRequestDto);

    TaskResponseDto updateById(Long id, TaskRequestDto taskRequestDto);

    TaskResponseDto updateStatusById(Long id, TaskStatusRequestDto taskStatusRequestDto);

    TaskResponseDto updateExecutorById(Long id, TaskExecutorRequestDto taskExecutorRequestDto);

    void deleteById(Long id);

}
