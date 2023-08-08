package com.example.staffmanagerapi.dto.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceSearchResponseDTO {
    private String name;
    private String customer;
    private String collaboratorFirstName;
    private String collaboratorLastName;
}
