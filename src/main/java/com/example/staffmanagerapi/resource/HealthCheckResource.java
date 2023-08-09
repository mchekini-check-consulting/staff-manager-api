package com.example.staffmanagerapi.resource;


import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/health")
@Slf4j
public class HealthCheckResource {

    @GetMapping
    public ResponseEntity healthCheck(@RequestHeader(value = "header1", required = false) String headerValue) {


        log.info("request received whith header value {}", headerValue);

        return ResponseEntity.status(200).build();
    }
}
