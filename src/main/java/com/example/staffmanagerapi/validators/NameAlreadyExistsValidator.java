package com.example.staffmanagerapi.validators;

import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameAlreadyExistsValidator implements ConstraintValidator<NameNotAlreadyExists, String> {

    private final CustomerRepository customerRepository;

    public NameAlreadyExistsValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !(customerRepository.existsByCustomerName(name));
    }
}
