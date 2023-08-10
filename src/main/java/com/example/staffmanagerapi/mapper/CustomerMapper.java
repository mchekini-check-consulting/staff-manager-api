package com.example.staffmanagerapi.mapper;

import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.dto.customer.CustomerDto;
import com.example.staffmanagerapi.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
  @Mapping(source = "customerId", target = "id")
  CustomerDto customerToCustomerDto(Customer customer);

  Customer CustomerDtoToCustomer(CustomerDto customer);

  CustomerCreationDto customerToCustomerCreationDto(Customer customer);
  Customer customerCreationDtoToCustomer(CustomerCreationDto customer);
}
