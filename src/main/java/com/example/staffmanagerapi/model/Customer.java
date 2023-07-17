package com.example.staffmanagerapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long customerId;
    private String customerEmail;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerTvaNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Set<Mission> missions;
}
