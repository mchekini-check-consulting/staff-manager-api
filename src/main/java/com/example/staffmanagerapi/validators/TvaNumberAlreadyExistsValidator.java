package com.example.staffmanagerapi.validators;

import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TvaNumberAlreadyExistsValidator implements ConstraintValidator<TvaNumberNotAlreadyExists, String> {

    private final CustomerRepository customerRepository;

    public TvaNumberAlreadyExistsValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(String tvaNumber, ConstraintValidatorContext constraintValidatorContext) {
        return !(customerRepository.existsByCustomerTvaNumber(tvaNumber));
    }
}
