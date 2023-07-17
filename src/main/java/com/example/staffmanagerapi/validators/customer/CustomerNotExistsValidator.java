package com.example.staffmanagerapi.validators.customer;

import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class CustomerNotExistsValidator implements ConstraintValidator<CustomerNotExists, Long> {

    private final CustomerRepository customerRepository;

    public CustomerNotExistsValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(Long customerId, ConstraintValidatorContext constraintValidatorContext) {
        if(Objects.isNull(customerId)){
            return true;
        }
        return customerRepository.findById(customerId).isPresent();
    }
}
