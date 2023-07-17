package com.example.staffmanagerapi.validators.mission;

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
@Constraint(validatedBy = MissionNotExistsValidator.class)
public @interface MissionNotExists {

    String message() default "La nom de la mission existe déjà";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

