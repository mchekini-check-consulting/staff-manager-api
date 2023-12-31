package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.invoice.InvoiceSearchRequestDTO;
import com.example.staffmanagerapi.dto.invoice.InvoiceSearchResponseDTO;
import com.example.staffmanagerapi.service.InvoiceService;
import com.example.staffmanagerapi.validators.invoice.InvoiceNameValid;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/invoice")
@Validated
public class InvoiceResource {
    private final InvoiceService invoiceService;

    public InvoiceResource (InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }
    @PostMapping("search")
    @Authenticated(authenticated = true, hasAnyRoles = {"admin"})
    public ResponseEntity<List> search(@Valid @RequestBody InvoiceSearchRequestDTO invoiceSearchRequestDto) throws Exception{
        log.info("tentative de recherche de factures avec le filtre suivant: {}", invoiceSearchRequestDto);

        List<Long> collaborators = invoiceSearchRequestDto.getCollaborators();
        List<Long> clients = invoiceSearchRequestDto.getClients();
        List<String> dates = invoiceSearchRequestDto.getDates();

        List<InvoiceSearchResponseDTO> invoices = invoiceService.search(collaborators, clients, dates);
        return ResponseEntity.status(HttpStatus.OK).body(invoices);
    }


    @GetMapping("/{invoiceName}")
    public ResponseEntity download(@PathVariable("invoiceName") @InvoiceNameValid String invoiceName){
        Object object = invoiceService.downloadFile(invoiceName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + invoiceName + "\"")
                .body(object);
    }
}
