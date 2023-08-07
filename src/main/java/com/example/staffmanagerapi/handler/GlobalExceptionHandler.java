package com.example.staffmanagerapi.handler;

import com.example.staffmanagerapi.enums.ErrorsEnum;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.staffmanagerapi.exception.*;
import com.example.staffmanagerapi.template.ResponseTemplate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ResponseTemplate> handleEntityNotFoundException(
    EntityNotFoundException ex
  ) {
    return ResponseEntity
      .status(HttpStatus.NOT_FOUND)
      .body(
        ResponseTemplate
          .builder()
          .error(ErrorsEnum.ENTITY_NOT_FOUND.toString())
          .message(ex.getMessage())
          .build()
      );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseTemplate> handleException(
    MethodArgumentNotValidException ex
  ) {
    BindingResult bindingResult = ex.getBindingResult();
    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    Map<String, String> errorMessages = new HashMap<>();

    for (FieldError fieldError : fieldErrors) {
      errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    ResponseTemplate response = ResponseTemplate
      .builder()
      .error(ErrorsEnum.VALIDATION_ERROR.toString())
      .message("Vos informations ne sont pas fiable.")
      .validations(errorMessages)
      .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ResponseTemplate> handleException(
    HttpMessageNotReadableException ex
  ) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ResponseTemplate
          .builder()
          .error(ErrorsEnum.HTTP_MESSAGE_NOT_READABLE.toString())
          .message(ex.getMessage())
          .build()
      );
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ResponseTemplate> handleException(
    BadRequestException ex
  ) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ResponseTemplate
          .builder()
          .error(ErrorsEnum.BAD_REQUEST.toString())
          .message(ex.getMessage())
          .build()
      );
  }

  @ExceptionHandler(
    value = {FileEmptyException.class }
  )
  public ResponseEntity<ResponseTemplate> handleBadRequestException(
    RuntimeException ex
  ) {
    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(
        ResponseTemplate
          .builder()
          .error(ErrorsEnum.RUNTIME_EXCEPTION.toString())
          .message(ex.getMessage())
          .build()
      );
  }

    @ExceptionHandler(
            value = { FileInvalidExtensionException.class }
    )
    public ResponseEntity<ResponseTemplate> handleUnsupportedMediaTypeException(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(
                        ResponseTemplate
                                .builder()
                                .error(ErrorsEnum.RUNTIME_EXCEPTION.toString())
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = { FileNameExistsException.class})
    public ResponseEntity<ResponseTemplate> handleConflictException(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseTemplate
                                .builder()
                                .error(ErrorsEnum.RUNTIME_EXCEPTION.toString())
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ResponseTemplate> handleConstraintViolationException(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseTemplate
                                .builder()
                                .error(ErrorsEnum.RUNTIME_EXCEPTION.toString())
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {AmazonS3Exception.class})
    public ResponseEntity<ResponseTemplate> handleIOException(
            RuntimeException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseTemplate
                                .builder()
                                .error(ErrorsEnum.RUNTIME_EXCEPTION.toString())
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {MultipleSocietiesFoundException.class})
    public ResponseEntity<ResponseTemplate> handleMultipleResultsReturnedWhenOnlyOneIsExpected(
            MultipleSocietiesFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ResponseTemplate
                                .builder()
                                .error(ErrorsEnum.RUNTIME_EXCEPTION.toString())
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {NoMissionFoundForCollaborator.class})
    public ResponseEntity<ResponseTemplate> handleNoMissionFoundForCollaborator(
            RuntimeException ex
    ) {
      return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ResponseTemplate
                                .builder()
                                .error(ErrorsEnum.ENTITY_NOT_FOUND.toString())
                                .message(ex.getMessage())
                                .build()
                );
    }
  @ExceptionHandler(value = {JRRuntimeException.class})
  public ResponseEntity<ResponseTemplate> handleJRRuntimeException(
          RuntimeException ex
  ) {
    String message = ex.getMessage() == null ? "Error de generation d'un Jasper report PDF " : ex.getMessage();
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                    ResponseTemplate
                            .builder()
                            .error(ErrorsEnum.RUNTIME_EXCEPTION.toString())
                            .message(message)
                            .build()
            );
  }
}
