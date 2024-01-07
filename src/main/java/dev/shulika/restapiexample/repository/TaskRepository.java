package dev.shulika.restapiexample.repository;

import dev.shulika.restapiexample.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByAuthorId(Long id, Pageable pageable);

    Page<Task> findByExecutorId(Long id, Pageable pageable);

    @Query(value = "select t from Task t left join fetch t.comments c where t.id = ?1 ")
    Optional<Task> findByIdWithComments(Long id);

    @Query(value = "select t from Task t left join fetch t.comments c where t.author.id = ?1 ")
    Page<Task> findByAuthorIdWithComments(Long id, Pageable pageable);
}