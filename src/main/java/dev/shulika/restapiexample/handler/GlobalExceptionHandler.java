package dev.shulika.restapiexample.handler;

import dev.shulika.restapiexample.exception.AlreadyExistsException;
import dev.shulika.restapiexample.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidException(MethodArgumentNotValidException exception) {
        log.error("IN GlobalExceptionHandler - MethodArgumentNotValidException - Message - {}", exception.getMessage());
        List<String> errorsList = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errorsList.add(error.getField() + " : " + error.getDefaultMessage()));
        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        log.error("IN GlobalExceptionHandler - NotFoundException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException exception) {
        log.error("IN GlobalExceptionHandler - AlreadyExistsException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleAlreadyExistsException(DataIntegrityViolationException exception) {
        log.error("IN GlobalExceptionHandler - DataIntegrityViolationException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        log.error("IN GlobalExceptionHandler - Exception - Message - {}", exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
