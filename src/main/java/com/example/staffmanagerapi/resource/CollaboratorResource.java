package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.service.CollaboratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.example.staffmanagerapi.model.Collaborator;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/collaborator")
public class CollaboratorResource {

    private final CollaboratorService collaboratorService;

    @Autowired
    public CollaboratorResource(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @PostMapping("")
    public ResponseEntity addCollaborator(@Valid @RequestBody Collaborator collaborator){
        System.out.println(collaborator);
        return collaboratorService.add(collaborator);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    
}
