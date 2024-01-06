package dev.shulika.restapiexample.repository;

import dev.shulika.restapiexample.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthorId(Long id, Pageable pageable);

    Page<Task> findByExecutorId(Long id, Pageable pageable);
}