package dev.shulika.restapiexample.service.impl;

import dev.shulika.restapiexample.dto.CommentDto;
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
    public Page<CommentDto> findAll(Pageable pageable) {
        log.info("IN CommentServiceImpl - findAll() - STARTED");
        Page<Comment> commentPage = commentRepository.findAll(pageable);
        return commentPage.map(commentMapper::toDto);
    }

    @Override
    public CommentDto findById(Long id) {
        log.info("IN CommentServiceImpl - findById() - STARTED");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found! id: " + id));
        return commentMapper.toDto(comment);
    }

    @Override
    @Transactional(readOnly = false)
    public Comment create(CommentDto commentDto) {
        log.info("IN CommentServiceImpl - create() - STARTED");
        return commentRepository.save(commentMapper.toEntity(commentDto));
    }

    @Override
    @Transactional(readOnly = false)
    public Comment updateById(Long id, CommentDto commentDto) {
        log.info("IN CommentServiceImpl - updateById() - STARTED");
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found! id: " + id));

        comment.setText(commentDto.getText());
        Task task = new Task();
        task.setId(commentDto.getTaskId());
        comment.setTask(task);

        return commentRepository.save(comment);
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
