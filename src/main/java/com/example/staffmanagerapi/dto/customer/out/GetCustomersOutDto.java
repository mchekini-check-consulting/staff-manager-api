package com.example.staffmanagerapi.dto.customer.out;

import com.example.staffmanagerapi.dto.customer.CustomerDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetCustomersOutDto {

  private List<CustomerDto> customers;
}
