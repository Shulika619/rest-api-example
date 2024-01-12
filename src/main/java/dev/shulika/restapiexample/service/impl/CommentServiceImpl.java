package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.comment.CommentRequestDto;
import dev.shulika.restapiexample.dto.comment.CommentResponseDto;
import dev.shulika.restapiexample.exception.NotFoundException;
import dev.shulika.restapiexample.mapper.CommentMapper;
import dev.shulika.restapiexample.model.Comment;
import dev.shulika.restapiexample.model.Person;
import dev.shulika.restapiexample.model.Task;
import dev.shulika.restapiexample.repository.CommentRepository;
import dev.shulika.restapiexample.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static dev.shulika.restapiexample.constant.CacheConst.COMMENTS;
import static dev.shulika.restapiexample.constant.ServiceConst.COMMENT_NOT_FOUND;

@Service
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
    @Cacheable(value = COMMENTS, key = "#id")
    public CommentResponseDto findById(Long id) {
        log.info("IN CommentServiceImpl - findById() - STARTED");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + id));
        return commentMapper.toDto(comment);
    }

    @Override
    public Page<CommentResponseDto> findByTaskId(Long taskId, Pageable pageable) {
        log.info("IN CommentServiceImpl - findByTask() - STARTED");
        Page<Comment> commentPage = commentRepository.findByTaskId(taskId, pageable);
        return commentPage.map(commentMapper::toDto);
    }

    @Override
    public CommentResponseDto create(CommentRequestDto commentRequestDto) {
        log.info("IN CommentServiceImpl - create() - STARTED");
        Comment savedComment = commentRepository.save(commentMapper.toEntity(commentRequestDto));
        return commentMapper.toDto(savedComment);
    }

    @Override
    @CachePut(value = COMMENTS, key = "#id")
    public CommentResponseDto updateById(Long id, CommentRequestDto commentRequestDto) {
        log.info("IN CommentServiceImpl - updateById() - STARTED");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + id));

        comment.setText(commentRequestDto.getText());
        Person author = new Person();
        author.setId(commentRequestDto.getAuthorId());
        comment.setAuthor(author);
        Task task = new Task();
        task.setId(commentRequestDto.getTaskId());
        comment.setTask(task);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toDto(savedComment);
    }

    @Override
    @CacheEvict(value = COMMENTS, key = "#id")
    public void deleteById(Long id) {
        log.info("IN CommentServiceImpl - deleteById() - STARTED");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND + id));
        commentRepository.delete(comment);
    }

}
