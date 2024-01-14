package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.task.*;
import dev.shulika.restapiexample.exception.NotFoundException;
import dev.shulika.restapiexample.mapper.TaskMapper;
import dev.shulika.restapiexample.model.Person;
import dev.shulika.restapiexample.model.Task;
import dev.shulika.restapiexample.repository.TaskRepository;
import dev.shulika.restapiexample.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static dev.shulika.restapiexample.constant.CacheConst.*;
import static dev.shulika.restapiexample.constant.ServiceConst.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    @Cacheable(PAGE_TASKS)
    public Page<TaskResponseDto> findAll(Pageable pageable) {
        log.info("IN TaskServiceImpl - findAll() - STARTED");
        Page<Task> taskPage = taskRepository.findAll(pageable);
        return taskPage.map(taskMapper::toDto);
    }

    @Override
    @Cacheable(value = TASKS, key = "#id")
    public TaskResponseDto findById(Long id) {
        log.info("IN TaskServiceImpl - findById() - STARTED");
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND + id));
        return taskMapper.toDto(task);
    }

    @Override
    @Cacheable(value = TASKS_COMMENTS, key = "#id")
    public TaskWithCommentDto findByIdWithComments(Long id) {
        log.info("IN TaskServiceImpl - findByIdWithComments() - STARTED");
        Task task = taskRepository.findByIdWithComments(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND + id));
        return taskMapper.toDtoWithComment(task);
    }

    @Override
    @Cacheable(PAGE_TASKS_AUTHOR)
    public Page<TaskResponseDto> findByAuthor(Long personId, Pageable pageable) {
        log.info("IN TaskServiceImpl - findByAuthor() - STARTED");
        Page<Task> taskPage = taskRepository.findByAuthorId(personId, pageable);
        return taskPage.map(taskMapper::toDto);
    }

    @Override
    public Page<TaskWithCommentDto> findByAuthorWithComments(Long personId, Pageable pageable) {
        log.info("IN TaskServiceImpl - findByAuthorWithComments() - STARTED");
        Page<Task> taskPage = taskRepository.findByAuthorIdWithComments(personId, pageable);
        return taskPage.map(taskMapper::toDtoWithComment);
    }

    @Override
    @Cacheable(PAGE_TASKS_EXECUTOR)
    public Page<TaskResponseDto> findByExecutor(Long personId, Pageable pageable) {
        log.info("IN TaskServiceImpl - findByExecutor() - STARTED");
        Page<Task> taskPage = taskRepository.findByExecutorId(personId, pageable);
        return taskPage.map(taskMapper::toDto);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = PAGE_TASKS, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_AUTHOR, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_EXECUTOR, allEntries = true)
            }
    )
    public TaskResponseDto create(TaskRequestDto taskRequestDto) {
        log.info("IN TaskServiceImpl - create() - STARTED");
        Task task = taskRepository.save(taskMapper.toEntity(taskRequestDto));
        return taskMapper.toDto(task);
    }

    @Override
    @Caching(
            put = {@CachePut(value = TASKS, key = "#id")},
            evict = {
                    @CacheEvict(value = TASKS_COMMENTS, key = "#id"),
                    @CacheEvict(value = PAGE_TASKS, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_AUTHOR, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_EXECUTOR, allEntries = true)
            }
    )
    public TaskResponseDto updateById(Long id, TaskRequestDto taskRequestDto) {
        log.info("IN TaskServiceImpl - updateById() - STARTED");
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND + id));

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
    @Caching(
            put = {@CachePut(value = TASKS, key = "#id")},
            evict = {
                    @CacheEvict(value = TASKS_COMMENTS, key = "#id"),
                    @CacheEvict(value = PAGE_TASKS, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_AUTHOR, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_EXECUTOR, allEntries = true)
            }
    )
    public TaskResponseDto updateStatusById(Long id, TaskStatusRequestDto taskStatusRequestDto) {
        log.info("IN TaskServiceImpl - updateStatusById() - STARTED");
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND + id));

        task.setStatus(taskStatusRequestDto.getStatus());
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    @Caching(
            put = {@CachePut(value = TASKS, key = "#id")},
            evict = {
                    @CacheEvict(value = TASKS_COMMENTS, key = "#id"),
                    @CacheEvict(value = PAGE_TASKS, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_AUTHOR, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_EXECUTOR, allEntries = true)
            }
    )
    public TaskResponseDto updateExecutorById(Long id, TaskExecutorRequestDto taskExecutorRequestDto) {
        log.info("IN TaskServiceImpl - updateExecutorById() - STARTED");
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND + id));

        Person person = new Person();
        person.setId(taskExecutorRequestDto.getExecutorId());
        task.setExecutor(person);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = TASKS, key = "#id"),
                    @CacheEvict(value = TASKS_COMMENTS, key = "#id"),
                    @CacheEvict(value = PAGE_TASKS, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_AUTHOR, allEntries = true),
                    @CacheEvict(value = PAGE_TASKS_EXECUTOR, allEntries = true)
            }
    )
    public void deleteById(Long id) {
        log.info("IN TaskServiceImpl - deleteById() - STARTED");
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TASK_NOT_FOUND + id));
        taskRepository.delete(task);
    }

}
