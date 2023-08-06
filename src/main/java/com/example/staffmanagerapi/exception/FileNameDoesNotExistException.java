package com.example.staffmanagerapi.exception;


public class FileNameDoesNotExistException extends RuntimeException {
    public FileNameDoesNotExistException(String message) {
        super(message);
    }
}