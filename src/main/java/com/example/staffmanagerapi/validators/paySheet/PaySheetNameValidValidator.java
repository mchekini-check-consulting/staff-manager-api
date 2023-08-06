package com.example.staffmanagerapi.validators.paySheet;

import com.example.staffmanagerapi.model.Paysheet;
import com.example.staffmanagerapi.repository.PaySheetRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;


public class PaySheetNameValidValidator implements ConstraintValidator<PaySheetNameValid,String> {

        private final PaySheetRepository paySheetRepository;

    public PaySheetNameValidValidator(PaySheetRepository paySheetRepository) {
        this.paySheetRepository = paySheetRepository;
    }

    @Override
    public boolean isValid(String documentName, ConstraintValidatorContext constraintValidatorContext) {
        Optional<Paysheet> paySheet = paySheetRepository.findByName(documentName);
        return paySheet.isPresent();
    }
}