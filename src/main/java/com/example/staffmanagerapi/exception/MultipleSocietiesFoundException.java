package com.example.staffmanagerapi.exception;

public class MultipleSocietiesFoundException extends RuntimeException {
    public MultipleSocietiesFoundException(String message) {
        super(message);
    }
}