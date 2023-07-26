package com.example.staffmanagerapi.exception;


public class FileInvalidExtensionException extends RuntimeException {
    public FileInvalidExtensionException(String message) {
        super(message);
    }
}