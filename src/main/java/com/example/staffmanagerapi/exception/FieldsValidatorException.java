package com.example.staffmanagerapi.exception;

import com.example.staffmanagerapi.response.FieldError;

import java.util.List;

public class FieldsValidatorException extends Throwable {
    private List<FieldError> errors;

    public FieldsValidatorException (List<FieldError> errors){
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
