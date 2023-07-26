package com.example.staffmanagerapi.validators.document;

import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.repository.DocumentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class DocumentNameValidValidator implements ConstraintValidator<DocumentNameValid,String> {

    private final DocumentRepository documentRepository;

    public DocumentNameValidValidator(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public boolean isValid(String documentName, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Document> document = documentRepository.findByName(documentName);
        return document.isPresent();
    }
}
