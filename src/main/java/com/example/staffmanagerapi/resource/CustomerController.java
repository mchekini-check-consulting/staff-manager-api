package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.response.FieldError;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.response.ResponseHandler;
import com.example.staffmanagerapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("api")
@RestController
@CrossOrigin(origins = "*")
@Component
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("customer")
    public ResponseEntity<?> add(@RequestBody @Valid CustomerCreationDto customer, BindingResult bindingResult) {
        try {
            if (customerRepository.existsByCustomerEmail(customer.getCustomerEmail())) {
                ResponseHandler responseHandler = new ResponseHandler(HttpStatus.BAD_REQUEST, "Email existe deja", HttpStatus.BAD_REQUEST.value(), null);
                return new ResponseEntity<>(responseHandler, HttpStatus.BAD_REQUEST);
            }

            if (bindingResult.hasErrors()) {
                List<FieldError> errorMessages = bindingResult.getFieldErrors().stream()
                        .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.toList());
                ResponseHandler responseHandler = new ResponseHandler(HttpStatus.BAD_REQUEST, null, HttpStatus.BAD_REQUEST.value(), errorMessages);
                return new ResponseEntity<>(responseHandler, HttpStatus.BAD_REQUEST);
            }
            Customer customerEntity = modelMapper.map(customer, Customer.class);
            ResponseHandler responseHandler = new ResponseHandler(null, null, HttpStatus.CREATED.value(), customerService.add(customerEntity));
            return new ResponseEntity<>(responseHandler, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

