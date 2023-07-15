package com.example.staffmanagerapi.validators;

import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomerNotExistsValidator implements ConstraintValidator<CustomerNotExists, Long> {

    private final CustomerRepository customerRepository;

    public CustomerNotExistsValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(Long customerId, ConstraintValidatorContext constraintValidatorContext) {
        return customerRepository.findById(customerId).isPresent();
    }
}
