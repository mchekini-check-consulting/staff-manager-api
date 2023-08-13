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
@Constraint(validatedBy = CustomerNotExistsValidator.class)
public @interface CustomerNotExists {

    String message() default "Le client n'existe pas";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

