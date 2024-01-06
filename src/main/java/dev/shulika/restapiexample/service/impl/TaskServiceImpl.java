package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.TaskRequestDto;
import dev.shulika.restapiexample.dto.TaskResponseDto;
import dev.shulika.restapiexample.exception.NotFoundException;
import dev.shulika.restapiexample.mapper.TaskMapper;
import dev.shulika.restapiexample.model.Person;
import dev.shulika.restapiexample.model.Task;
import dev.shulika.restapiexample.repository.TaskRepository;
import dev.shulika.restapiexample.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Page<TaskResponseDto> findAll(Pageable pageable) {
        log.info("IN TaskServiceImpl - findAll() - STARTED");
        Page<Task> taskPage = taskRepository.findAll(pageable);
        return taskPage.map(taskMapper::toDto);
    }

    @Override
    public TaskResponseDto findById(Long id) {
        log.info("IN TaskServiceImpl - findById() - STARTED");
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found! id: " + id));
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional(readOnly = false)
    public TaskResponseDto create(TaskRequestDto taskRequestDto) {
        log.info("IN TaskServiceImpl - create() - STARTED");
        Task task = taskRepository.save(taskMapper.toEntity(taskRequestDto));
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional(readOnly = false)
    public TaskResponseDto updateById(Long id, TaskRequestDto taskRequestDto) {
        log.info("IN TaskServiceImpl - updateById() - STARTED");
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found! id: " + id));

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setStatus(taskRequestDto.getStatus());
        task.setPriority(taskRequestDto.getPriority());

        Person author = new Person();
        author.setId(taskRequestDto.getAuthorId());
        task.setAuthor(author);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteById(Long id) {
        log.info("IN TaskServiceImpl - deleteById() - STARTED");
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task not found! id: " + id);
        }
        taskRepository.deleteById(id);
    }

}
