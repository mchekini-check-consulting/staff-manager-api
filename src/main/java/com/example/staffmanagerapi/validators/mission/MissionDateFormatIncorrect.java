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
@Constraint(validatedBy = MissionDateFormatIncorrectValidator.class)
public @interface MissionDateFormatIncorrect {
    String message() default "La date est incorrecte";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
