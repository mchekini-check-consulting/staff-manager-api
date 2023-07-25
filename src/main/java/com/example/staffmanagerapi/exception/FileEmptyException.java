package com.example.staffmanagerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class FileEmptyException extends RuntimeException {
    public FileEmptyException(String message) {
        super(message);
    }
}