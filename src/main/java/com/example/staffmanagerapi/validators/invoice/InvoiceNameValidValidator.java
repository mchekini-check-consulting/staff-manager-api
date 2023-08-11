package com.example.staffmanagerapi.validators.invoice;

import com.example.staffmanagerapi.model.Invoice;
import com.example.staffmanagerapi.repository.InvoiceRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class InvoiceNameValidValidator implements ConstraintValidator<InvoiceNameValid, String> {

    private final InvoiceRepository invoiceRepository;

    public InvoiceNameValidValidator(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public boolean isValid(String invoiceName, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Invoice> invoice = invoiceRepository.findByName(invoiceName);
        return invoice.isPresent();
    }
}



