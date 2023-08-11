package com.example.staffmanagerapi.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.staffmanagerapi.dto.invoice.InvoiceSearchResponseDTO;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.exception.NotFoundException;
import com.example.staffmanagerapi.model.Invoice;
import com.example.staffmanagerapi.repository.InvoiceRepository;
import com.example.staffmanagerapi.utils.InvoiceSpecifications;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InvoiceService {

    @Value("${bucket.factures}")
    public String bucketInvoiceName;


    private final InvoiceRepository invoiceRepository;
    private final AmazonS3Service amazonS3Service;

    public InvoiceService(InvoiceRepository invoiceRepository, AmazonS3Service amazonS3Service) {
        this.invoiceRepository = invoiceRepository;
        this.amazonS3Service = amazonS3Service;
    }

    public List<InvoiceSearchResponseDTO> search(@Valid List<Long> collaborators, List<Long> clients, List<String> dates) {
        Specification<Invoice> spec = Specification.where(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

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
                                .id(invoice.getId())
                                .date(invoice.getMonthYear().format(formatter))
                                .name(invoice.getName())
                                .customer(invoice.getCustomer().getCustomerName())
                                .collaboratorLastName(invoice.getCollaborator().getLastName())
                                .collaboratorFirstName(invoice.getCollaborator().getFirstName())
                                .build()
                ).toList();
        return invoices;
    }

    public byte[] downloadFile(String invoiceName) {
        if (amazonS3Service.bucketNotExistOrEmpty(bucketInvoiceName)) {
            throw new BadRequestException("la bucket n'existe pas ou est vide");
        }
        try {

            final S3Object s3Object = amazonS3Service.download(bucketInvoiceName, invoiceName);
            final S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

            byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
            log.info("File downloaded successfully.");
            s3Object.close();
            return content;
        } catch (AmazonS3Exception e) {
            log.error("Error Message= " + e.getMessage());
            throw new NotFoundException(e.getMessage());
        } catch (final Exception ex) {
            log.error("Error Message= " + ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }
    }
}
