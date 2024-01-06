package dev.shulika.restapiexample.service;

import dev.shulika.restapiexample.dto.comment.CommentRequestDto;
import dev.shulika.restapiexample.dto.comment.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<CommentResponseDto> findAll(Pageable pageable);

    CommentResponseDto findById(Long id);

    CommentResponseDto create(CommentRequestDto commentRequestDto);

    CommentResponseDto updateById(Long id, CommentRequestDto commentRequestDto);

    void deleteById(Long id);

}
