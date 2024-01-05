package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.CommentDto;
import dev.shulika.restapiexample.model.Comment;
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
    public Page<CommentDto> getAll(Pageable pageable) {
        return commentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@Valid @PathVariable Long id) {
        CommentDto commentDto = commentService.findById(id);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comment> create(@Valid @RequestBody CommentDto commentDto) {
        Comment comment = commentService.create(commentDto);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> update(@Valid @PathVariable Long id, @Valid @RequestBody CommentDto commentDto) {
        Comment comment = commentService.updateById(id, commentDto);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@Valid @PathVariable Long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

}
