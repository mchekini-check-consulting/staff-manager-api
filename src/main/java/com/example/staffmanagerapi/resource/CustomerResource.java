package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
}

