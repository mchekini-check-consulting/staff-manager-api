package com.example.staffmanagerapi.validators.document;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DocumentNameValidValidator.class)
public @interface DocumentNameValid {
    String message() default "La document n'existe pas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
