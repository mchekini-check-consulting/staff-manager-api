package com.example.staffmanagerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileEmptyException extends RuntimeException {
    public FileEmptyException(String message) {
        super(message);
    }
}