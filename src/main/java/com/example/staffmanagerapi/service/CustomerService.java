package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;


    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public void add(CustomerCreationDto customerDto) {

        Customer customerEntity = modelMapper.map(customerDto, Customer.class);
        customerRepository.save(customerEntity);
    }
}
