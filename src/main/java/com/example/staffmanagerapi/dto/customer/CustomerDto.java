package com.example.staffmanagerapi.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {

  private Long id;
  private String customerEmail;
  private String customerName;
  private String customerAddress;
  private String customerPhone;
  private String customerTvaNumber;
}
