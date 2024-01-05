package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.CommentDto;
import dev.shulika.restapiexample.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<CommentDto> findAll(Pageable pageable);

    CommentDto findById(Long id);

    Comment create(CommentDto commentDto);

    Comment updateById(Long id, CommentDto commentDto);

    void deleteById(Long id);

}
