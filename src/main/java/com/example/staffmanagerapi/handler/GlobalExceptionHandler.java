package com.example.staffmanagerapi.handler;

import com.example.staffmanagerapi.exception.BadRequestException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    List<String> errorMessages = new ArrayList<>();

    for (FieldError fieldError : fieldErrors) {
      String fieldName = fieldError.getField();
      String errorMessage = fieldError.getDefaultMessage();
      String fullErrorMessage = fieldName + ": " + errorMessage;
      errorMessages.add(fullErrorMessage);
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

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
