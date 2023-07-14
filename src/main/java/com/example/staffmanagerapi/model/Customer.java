package com.example.staffmanagerapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name="customer_seq", sequenceName = "customer_seq", allocationSize = 1)
    private Long customerId;

    private String customerEmail;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerTvaNumber;
}
