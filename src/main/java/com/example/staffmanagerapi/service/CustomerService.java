package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.dto.customer.CustomerDto;
import com.example.staffmanagerapi.dto.customer.CustomerUpdateInputDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final ModelMapper modelMapper;

  public CustomerService(
    CustomerRepository customerRepository,
    ModelMapper modelMapper
  ) {
    this.customerRepository = customerRepository;
    this.modelMapper = modelMapper;
  }

  public void add(CustomerCreationDto customerDto) {
    Customer customerEntity = modelMapper.map(customerDto, Customer.class);
    customerRepository.save(customerEntity);
  }

  public List<CustomerDto> getCustomers() {
    List<Customer> customers = this.customerRepository.findAll(Sort.by(Sort.Direction.DESC, "customerId"));

    modelMapper
      .typeMap(Customer.class, CustomerDto.class)
      .addMapping(Customer::getCustomerId, CustomerDto::setId);

    List<CustomerDto> parsedCustomers = modelMapper.map(
      customers,
      new TypeToken<List<CustomerDto>>() {}.getType()
    );

    return parsedCustomers;
  }

  public void update(Long customerId,CustomerUpdateInputDto customerUpdateInputDto){
    if(!customerId.equals(customerUpdateInputDto.getId())){
      throw new BadRequestException("Id différents");
    }
    Optional<Customer> emailExistCustomer = customerRepository.findByCustomerEmailAndCustomerIdIsNot(customerUpdateInputDto.getCustomerEmail(),customerUpdateInputDto.getId());
    if(emailExistCustomer.isPresent()){
      throw new BadRequestException("Email existe déjà");
    }
    Optional<Customer> nameExistCustomer = customerRepository.findByCustomerNameAndCustomerIdIsNot(customerUpdateInputDto.getCustomerName(),customerUpdateInputDto.getId());
    if(nameExistCustomer.isPresent()){
      throw new BadRequestException("Nom existe déjà");
    }

    Optional<Customer> phoneExistCustomer = customerRepository.findByCustomerPhoneAndCustomerIdIsNot(customerUpdateInputDto.getCustomerPhone(),customerUpdateInputDto.getId());
    if(phoneExistCustomer.isPresent()){
      throw new BadRequestException("Téléphone existe déjà");
    }

    Optional<Customer> tvaExistCustomer = customerRepository.findByCustomerTvaNumberAndCustomerIdIsNot(customerUpdateInputDto.getCustomerTvaNumber(),customerUpdateInputDto.getId());
    if(tvaExistCustomer.isPresent()){
      throw new BadRequestException("TVA existe déjà");
    }
    Customer customer = customerRepository.findByCustomerId(customerUpdateInputDto.getId()).get();

    customer.setCustomerEmail(customerUpdateInputDto.getCustomerEmail().toUpperCase());
    customer.setCustomerName(customerUpdateInputDto.getCustomerName());
    customer.setCustomerAddress(customerUpdateInputDto.getCustomerAddress());
    customer.setCustomerPhone(customerUpdateInputDto.getCustomerPhone());
    customer.setCustomerTvaNumber(customerUpdateInputDto.getCustomerTvaNumber());

    customerRepository.save(customer);
  }
}
