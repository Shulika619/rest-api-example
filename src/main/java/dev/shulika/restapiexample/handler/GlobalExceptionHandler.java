package dev.shulika.restapiexample.handler;

import dev.shulika.restapiexample.exception.AlreadyExistsException;
import dev.shulika.restapiexample.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static dev.shulika.restapiexample.constant.ServiceConst.ACCESS_DENIED;
import static dev.shulika.restapiexample.constant.ServiceConst.BAD_CREDENTIALS;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException exception) {
        log.error("IN GlobalExceptionHandler - handleBadCredentialsException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
//    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException exception) {
        log.error("IN GlobalExceptionHandler - handleAccessDeniedException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(ACCESS_DENIED, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<String>> handleValidException(MethodArgumentNotValidException exception) {
        log.error("IN GlobalExceptionHandler - MethodArgumentNotValidException - Message - {}", exception.getMessage());
        List<String> errorsList = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errorsList.add(error.getField() + " : " + error.getDefaultMessage()));
        return new ResponseEntity<>(errorsList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(NotFoundException exception) {
        log.error("IN GlobalExceptionHandler - NotFoundException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException exception) {
        log.error("IN GlobalExceptionHandler - AlreadyExistsException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAlreadyExistsException(DataIntegrityViolationException exception) {
        log.error("IN GlobalExceptionHandler - DataIntegrityViolationException - Message - {}", exception.getMessage());
        return new ResponseEntity<>(exception.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleException(Exception exception) {
        log.error("IN GlobalExceptionHandler - Exception - Message - {}", exception.getMessage(), exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
