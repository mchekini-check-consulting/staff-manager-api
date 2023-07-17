package com.example.staffmanagerapi.validators.customer;

import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomerAlreadyExistsValidator implements ConstraintValidator<EmailNotAlreadyExists, String> {

    private final CustomerRepository customerRepository;

    public CustomerAlreadyExistsValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !(customerRepository.existsByCustomerEmail(email));
    }
}
