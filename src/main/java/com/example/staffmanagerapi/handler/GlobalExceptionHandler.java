package com.example.staffmanagerapi.handler;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.exception.FileNameExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(
            MethodArgumentNotValidException ex
    ) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> errorMessages = new HashMap<>();

        for (FieldError fieldError : fieldErrors) {
            errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleException(
            HttpMessageNotReadableException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = {FileEmptyException.class})
    public ResponseEntity<Object> handleBadRequestException(
           RuntimeException ex
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = {FileInvalidExtensionException.class})
    public ResponseEntity<Object> handleUnsupportedMediaTypeException(
            RuntimeException ex
    ) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(ex.getMessage());
    }

    @ExceptionHandler(value = { FileNameExistsException.class})
    public ResponseEntity<Object> handleConflictException(
            RuntimeException ex
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolationException(
            RuntimeException ex
    ) {;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = {AmazonS3Exception.class})
    public ResponseEntity<Object> handleIOException(
            RuntimeException ex
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
