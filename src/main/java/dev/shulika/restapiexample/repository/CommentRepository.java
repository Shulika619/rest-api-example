package dev.shulika.restapiexample.repository;

import dev.shulika.restapiexample.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTaskId(Long id, Pageable pageable);
}