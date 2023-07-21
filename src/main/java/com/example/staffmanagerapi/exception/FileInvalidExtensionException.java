package com.example.staffmanagerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileInvalidExtensionException extends RuntimeException {
    public FileInvalidExtensionException(String message) {
        super(message);
    }
}