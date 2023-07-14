package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.exception.FieldsValidatorException;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.response.FieldError;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.response.ResponseHandler;
import com.example.staffmanagerapi.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("api")
@RestController
@Component
@Slf4j
public class CustomerController {

    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<?> Response(HttpStatus httpStatus, Integer statusCode, Object errorMessages) {
        ResponseHandler responseHandler = new ResponseHandler(httpStatus, statusCode, errorMessages);
        if (httpStatus == HttpStatus.BAD_REQUEST)
            return new ResponseEntity<>(responseHandler, HttpStatus.BAD_REQUEST);
        if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR)
            return new ResponseEntity<>(responseHandler, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(responseHandler, HttpStatus.CREATED);
    }

    @PostMapping("customer")
    public ResponseEntity<?> add(@RequestBody @Valid CustomerCreationDto customer, BindingResult bindingResult) {
        try {
            Customer customerEntity = modelMapper.map(customer, Customer.class);
            return Response(HttpStatus.CREATED, HttpStatus.CREATED.value(), customerService.add(customerEntity, bindingResult));
        } catch (FieldsValidatorException e) {
            return Response(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), e.getErrors());
        } catch (Exception e) {
            return Response(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }
    }
}

