package com.example.staffmanagerapi.validators.customer;

import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.repository.CustomerRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class CustomerIdNotExistValidator implements ConstraintValidator<CustomerIdNotExist, Long> {
    private final CustomerRepository customerRepository;

    public CustomerIdNotExistValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        if(id.equals(null)){
            return false;
        }
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.isPresent();
    }
}
