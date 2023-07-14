package com.example.staffmanagerapi.unit;

import com.example.staffmanagerapi.dto.CustomerCreationDto;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerUnitTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    void ItShoulAddNewCustomer() throws Exception {
        Customer expectedCustomer = new Customer(30L, "testUnit1@gmal.com", "test unit 1", "Algerie", "0341991118", "fr01234567890");
        when(customerRepository.save(expectedCustomer)).thenReturn(expectedCustomer);

//        Customer actualCustomer = customerService.add(expectedCustomer);
//
//        verify(customerRepository).save(expectedCustomer);
//
//        assertEquals(expectedCustomer, actualCustomer);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

}
