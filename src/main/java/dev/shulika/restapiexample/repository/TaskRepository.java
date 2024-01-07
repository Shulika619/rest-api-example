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

    /**
     * Without join fetch + @BatchSize(size = 50) @OneToMany List<Comment> comments in Task
     * @Query(value = "select t from Task t left join fetch t.comments c where t.author.id = ?1 ")
     * join fetch WARN -> HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory
     */
    @Query(value = "select t from Task t where t.author.id = ?1 ")
    Page<Task> findByAuthorIdWithComments(Long id, Pageable pageable);
}