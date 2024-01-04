package dev.shulika.restapiexample.repository;

import dev.shulika.restapiexample.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}