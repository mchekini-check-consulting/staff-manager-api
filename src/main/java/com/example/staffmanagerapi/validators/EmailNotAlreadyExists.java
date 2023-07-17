package com.example.staffmanagerapi.validators;


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
@Constraint(validatedBy = EmailAlreadyExistsValidator.class)
public @interface EmailNotAlreadyExists {

    String message() default "Email Already Exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
