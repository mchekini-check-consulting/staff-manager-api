package com.example.staffmanagerapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FileNameExistsException extends RuntimeException {
    public FileNameExistsException(String message) {
        super(message);
    }
}