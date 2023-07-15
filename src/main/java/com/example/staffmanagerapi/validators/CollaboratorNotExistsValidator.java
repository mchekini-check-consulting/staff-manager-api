package com.example.staffmanagerapi.validators;

import com.example.staffmanagerapi.repository.CollaboratorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CollaboratorNotExistsValidator implements ConstraintValidator<CollaboratorNotExists, Long> {

    private final CollaboratorRepository collaboratorRepository;

    public CollaboratorNotExistsValidator(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    @Override
    public boolean isValid(Long collaboratorId, ConstraintValidatorContext constraintValidatorContext) {
        return collaboratorRepository.findById(collaboratorId).isPresent();
    }
}
