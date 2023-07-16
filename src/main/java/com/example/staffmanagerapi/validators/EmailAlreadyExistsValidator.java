package com.example.staffmanagerapi.validators;

import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailAlreadyExistsValidator implements ConstraintValidator<EmailNotAlreadyExists, String> {

    private final CustomerRepository customerRepository;

    public EmailAlreadyExistsValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !(customerRepository.existsByCustomerEmail(email));
    }
}
