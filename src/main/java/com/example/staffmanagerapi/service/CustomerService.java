package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.exception.FieldsValidatorException;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.response.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<FieldError> GetErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return errors;
    }

    public Customer add(Customer customer, BindingResult bindingResult) throws FieldsValidatorException {
        List<FieldError> errorMessages;
        Boolean isExist = customerRepository.existsByCustomerEmail(customer.getCustomerEmail());

        if (bindingResult.hasErrors() || isExist) {
            errorMessages = GetErrors(bindingResult);
            if (isExist) {
                FieldError field = new FieldError("customerEmail", "Email existe déja");
                errorMessages.add(field);
            }
            throw new FieldsValidatorException(errorMessages);
        }
        return customerRepository.save(customer);
    }
}
