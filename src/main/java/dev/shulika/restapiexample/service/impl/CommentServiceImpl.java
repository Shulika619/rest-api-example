package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.CommentRequestDto;
import dev.shulika.restapiexample.dto.CommentResponseDto;
import dev.shulika.restapiexample.exception.NotFoundException;
import dev.shulika.restapiexample.mapper.CommentMapper;
import dev.shulika.restapiexample.model.Comment;
import dev.shulika.restapiexample.model.Task;
import dev.shulika.restapiexample.repository.CommentRepository;
import dev.shulika.restapiexample.service.CommentService;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public Page<CommentResponseDto> findAll(Pageable pageable) {
        log.info("IN CommentServiceImpl - findAll() - STARTED");
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        return commentPage.map(commentMapper::toDto);
    }

    @Override
    public CommentResponseDto findById(Long id) {
        log.info("IN CommentServiceImpl - findById() - STARTED");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found! id: " + id));
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = false)
    public CommentResponseDto create(CommentRequestDto commentRequestDto) {
        log.info("IN CommentServiceImpl - create() - STARTED");
        Comment savedComment = commentRepository.save(commentMapper.toEntity(commentRequestDto));
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional(readOnly = false)
    public CommentResponseDto updateById(Long id, CommentRequestDto commentRequestDto) {
        log.info("IN CommentServiceImpl - updateById() - STARTED");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found! id: " + id));

        comment.setText(commentRequestDto.getText());
        Task task = new Task();
        task.setId(commentRequestDto.getTaskId());
        comment.setTask(task);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteById(Long id) {
        log.info("IN CommentServiceImpl - deleteById() - STARTED");
        if (!commentRepository.existsById(id)) {
            throw new NotFoundException("Comment not found! id: " + id);
        }
        commentRepository.deleteById(id);
    }

}
