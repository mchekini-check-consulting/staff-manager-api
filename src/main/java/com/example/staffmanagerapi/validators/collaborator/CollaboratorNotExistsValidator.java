package com.example.staffmanagerapi.validators.collaborator;

import com.example.staffmanagerapi.repository.CollaboratorRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class CollaboratorNotExistsValidator implements ConstraintValidator<CollaboratorNotExists, Long> {

    private final CollaboratorRepository collaboratorRepository;

    public CollaboratorNotExistsValidator(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    @Override
    public boolean isValid(Long collaboratorId, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(collaboratorId)){
            return true;
        }
        return collaboratorRepository.findById(collaboratorId).isPresent();
    }
}
