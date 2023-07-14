package com.example.staffmanagerapi.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHandler {
    private Object error;
    private String message;
    private Integer statusCode;
    private Object data;
}
