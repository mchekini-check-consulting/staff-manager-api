package com.example.staffmanagerapi.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/healthcheck")
public class HealthCheckResource {

  @GetMapping
  public ResponseEntity check() {
    return ResponseEntity.ok().build();
  }
}
