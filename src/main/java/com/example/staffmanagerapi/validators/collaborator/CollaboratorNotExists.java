package com.example.staffmanagerapi.validators.collaborator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CollaboratorNotExistsValidator.class)
public @interface CollaboratorNotExists {

    String message() default "Le collaborateur n'existe pas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


