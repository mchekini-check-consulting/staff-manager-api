package com.example.staffmanagerapi.validators.document;


import com.example.staffmanagerapi.repository.DocumentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentNotExistsValidator implements ConstraintValidator<DocumentNotExists, String> {

    private final DocumentRepository documentRepository;

    public DocumentNotExistsValidator(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !documentRepository.existsByName(name);
    }
}