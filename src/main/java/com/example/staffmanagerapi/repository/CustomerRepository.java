package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByCustomerEmail(String email);
    Optional<Customer> findByCustomerId(Long id);
    Optional<Customer> findByCustomerEmailAndCustomerIdIsNot(String email, Long id);
    Optional<Customer> findByCustomerNameAndCustomerIdIsNot(String name, Long id);
    Optional<Customer> findByCustomerPhoneAndCustomerIdIsNot(String phone, Long id);
    Optional<Customer> findByCustomerTvaNumberAndCustomerIdIsNot(String phone, Long id);

    boolean existsByCustomerEmail(String email);

    boolean existsByCustomerName(String name);
    boolean existsByCustomerTvaNumber(String tvaNumber);
    boolean existsByCustomerPhone(String phone);
}
