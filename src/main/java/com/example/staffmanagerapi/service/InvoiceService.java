package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.dto.invoice.InvoiceSearchRequestDTO;
import com.example.staffmanagerapi.dto.invoice.InvoiceSearchResponseDTO;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.model.Invoice;
import com.example.staffmanagerapi.repository.InvoiceRepository;
import com.example.staffmanagerapi.utils.DocumentSpecifications;
import com.example.staffmanagerapi.utils.InvoiceSpecifications;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService (InvoiceRepository invoiceRepository){
        this.invoiceRepository= invoiceRepository;
    }
    public List<InvoiceSearchResponseDTO> search(@Valid List<Long> collaborators, List<Long> clients, List<String> dates){
        Specification<Invoice> spec = Specification.where(null);

        if (collaborators != null && !collaborators.isEmpty()) {
            spec = spec.and(InvoiceSpecifications.withCollaboratorIds(collaborators));
        }

        if (clients != null && !clients.isEmpty()) {
            spec = spec.and(InvoiceSpecifications.withCustomers(clients));
        }

        if (dates != null && !dates.isEmpty()) {
            List<LocalDate> listDates = dates.stream()
                    .map(date -> {
                        String[] parts = date.split("/");
                        int year = Integer.parseInt(parts[1]);
                        int month = Integer.parseInt(parts[0]);
                        return YearMonth.of(year, month).atDay(1);
                    })
                    .collect(Collectors.toList());
            spec = spec.and(InvoiceSpecifications.withDates(listDates));
        }

        List<Invoice> response = invoiceRepository.findAll(spec);
        List<InvoiceSearchResponseDTO> invoices =
                response.stream().map(
                        invoice -> InvoiceSearchResponseDTO.builder()
                                .name(invoice.getName())
                                .customer(invoice.getCustomer().getCustomerName())
                                .collaboratorLastName(invoice.getCollaborator().getLastName())
                                .collaboratorFirstName(invoice.getCollaborator().getFirstName())
                                .build()
                ).toList();
        return invoices;
    }
}
