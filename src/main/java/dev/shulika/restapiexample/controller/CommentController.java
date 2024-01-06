package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.comment.CommentRequestDto;
import dev.shulika.restapiexample.dto.comment.CommentResponseDto;
import dev.shulika.restapiexample.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public Page<CommentResponseDto> getAll(Pageable pageable) {
        return commentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getById(@Valid @PathVariable Long id) {
        CommentResponseDto commentResponseDto = commentService.findById(id);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    @GetMapping("/task/{taskId}")
    public Page<CommentResponseDto> getById(@Valid @PathVariable Long taskId, Pageable pageable) {
        return commentService.findByTaskId(taskId, pageable);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> create(@Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.create(commentRequestDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(@Valid @PathVariable Long id, @Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.updateById(id, commentRequestDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @PathVariable Long id) {
        commentService.deleteById(id);
    }

}
