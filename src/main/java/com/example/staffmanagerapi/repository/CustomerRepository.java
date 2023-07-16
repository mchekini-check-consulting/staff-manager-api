package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCustomerEmail(String email);

    boolean existsByCustomerName(String name);
    boolean existsByCustomerTvaNumber(String tvaNumber);
    boolean existsByCustomerPhone(String phone);
}
