package com.example.staffmanagerapi.validators.paySheet;

import com.example.staffmanagerapi.validators.document.DocumentNameValidValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = PaySheetNameValidValidator.class)
public @interface PaySheetNameValid {
    String message() default "La fiche de paie n'existe pas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
