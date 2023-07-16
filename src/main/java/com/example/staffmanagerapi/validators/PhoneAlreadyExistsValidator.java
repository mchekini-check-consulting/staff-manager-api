package com.example.staffmanagerapi.validators;

import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneAlreadyExistsValidator implements ConstraintValidator<PhoneNotAlreadyExists, String> {
    private final CustomerRepository customerRepository;

    public PhoneAlreadyExistsValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return !(customerRepository.existsByCustomerPhone(phone));
    }
}
