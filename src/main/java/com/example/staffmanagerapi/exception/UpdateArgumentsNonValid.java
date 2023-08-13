package com.example.staffmanagerapi.exception;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateArgumentsNonValid extends RuntimeException{
    Map<String, String> errors;
    public UpdateArgumentsNonValid(String message, Map<String,String> errors){
        super(message);
        this.errors = errors;
    }
}
