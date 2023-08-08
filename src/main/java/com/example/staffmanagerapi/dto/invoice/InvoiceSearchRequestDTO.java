package com.example.staffmanagerapi.dto.invoice;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceSearchRequestDTO {
    @Valid
    private List<Long> collaborators;
    @Valid
    private List<Long> clients;
    @Valid
    private List<String> dates;
}
