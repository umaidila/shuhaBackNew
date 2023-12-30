package ru.shuha.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ElementAlreadyExistsException.class)
    public ResponseEntity<Object> handleElementAlreadyExistException(ElementAlreadyExistsException ex) {
        var apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.CONFLICT,
                LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }


    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> validationList = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        return new ResponseEntity<>(validationList, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Object> handleElementNotFoundException(ElementNotFoundException ex) {
        var apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidLoginException.class})
    public ResponseEntity<Object> handleInvalidLoginException(InvalidLoginException ex) {
        var apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PreconditionFailedException.class)
    public ResponseEntity<Object> handlePreconditionFailedException(PreconditionFailedException ex) {
        var apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.PRECONDITION_FAILED,
                LocalDateTime.now());
        return new ResponseEntity<>(apiError, HttpStatus.PRECONDITION_FAILED);
    }

}
