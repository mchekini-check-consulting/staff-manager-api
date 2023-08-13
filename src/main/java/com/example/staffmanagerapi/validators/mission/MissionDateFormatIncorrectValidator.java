package com.example.staffmanagerapi.validators.mission;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class MissionDateFormatIncorrectValidator implements ConstraintValidator<MissionDateFormatIncorrect, String> {
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        try {
            // JAVA LOCALDATE USES uuuu FOR YEARS AND YYYY for ERA
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
