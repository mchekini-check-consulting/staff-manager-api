package com.example.staffmanagerapi.validators.customer;

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
@Constraint(validatedBy = CustomerIdNotExistValidator.class)
public @interface CustomerIdNotExist {
    String message() default "L'Id du client n'existe pas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
