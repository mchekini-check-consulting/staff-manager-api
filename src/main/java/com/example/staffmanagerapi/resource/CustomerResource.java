package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.dto.customer.CustomerDto;
import com.example.staffmanagerapi.dto.customer.out.GetCustomersOutDto;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("api/v1/customer")
@RestController
@Component
@Slf4j
public class CustomerResource {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity add(@RequestBody @Valid CustomerCreationDto customerDto) {

        log.info("Tentative de création du client {}", customerDto.getCustomerName());
        customerService.add(customerDto);
        log.info("Création du client {} effectuée avec succès", customerDto.getCustomerName());

        return ResponseEntity.status(201).build();
    }

    @GetMapping 
    public ResponseEntity<GetCustomersOutDto> getCustomers() {
        List<Customer> customers = this.customerService.getCustomers();
        List<CustomerDto> parsedCustomers = customers
          .stream()
          .map(row ->
            CustomerDto
              .builder()
              .id(row.getCustomerId())
              .customerEmail(row.getCustomerEmail())
              .customerName(row.getCustomerName())
              .customerAddress(row.getCustomerAddress())
              .customerPhone(row.getCustomerPhone())
              .customerTvaNumber(row.getCustomerTvaNumber())
              .build()
          )
          .toList();
        GetCustomersOutDto parsedOutput = GetCustomersOutDto.builder().customers(parsedCustomers).build();
        return ResponseEntity.status(200).body(parsedOutput);
    }
}

