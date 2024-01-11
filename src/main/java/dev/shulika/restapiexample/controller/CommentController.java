package dev.shulika.restapiexample.controller;

import dev.shulika.restapiexample.dto.comment.CommentRequestDto;
import dev.shulika.restapiexample.dto.comment.CommentResponseDto;
import dev.shulika.restapiexample.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Comment", description = "Comment management APIs")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Get comments", description = "Provides all comments, supports pagination and filtering")
    @GetMapping
    public Page<CommentResponseDto> getAll(Pageable pageable) {
        return commentService.findAll(pageable);
    }

    @Operation(summary = "Get comment", description = "Provides comment by id")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDto> getById(@Valid @PathVariable Long id) {
        CommentResponseDto commentResponseDto = commentService.findById(id);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    @Operation(
            summary = "Get comments by task id",
            description = "Provides all comments with the specified task id, supports pagination and filtering")
    @GetMapping("/tasks/{taskId}")
    public Page<CommentResponseDto> getById(@Valid @PathVariable Long taskId, Pageable pageable) {
        return commentService.findByTaskId(taskId, pageable);
    }

    @Operation(summary = "Create comment", description = "Allows you to create a comment")
    @PostMapping
    public ResponseEntity<CommentResponseDto> create(@Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.create(commentRequestDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Update comment", description = "Allows you to update a comment by its id")
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> update(
            @Valid @PathVariable Long id,
            @Valid @RequestBody CommentRequestDto commentRequestDto) {
        CommentResponseDto commentResponseDto = commentService.updateById(id, commentRequestDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    @Operation(summary = "Delete comment", description = "Allows you to delete a comment by its id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Valid @PathVariable Long id) {
        commentService.deleteById(id);
    }

}
