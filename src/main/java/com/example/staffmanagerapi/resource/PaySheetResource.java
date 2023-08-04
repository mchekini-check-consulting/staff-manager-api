package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.dto.paysheet.CreatePaySheetDTO;
import com.example.staffmanagerapi.service.PaySheetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/paysheet")
public class PaySheetResource {

    private final PaySheetService paySheetService;

    public PaySheetResource(PaySheetService paySheetService) {
        this.paySheetService = paySheetService;
    }

    @PostMapping()
    public ResponseEntity<?> upload(@ModelAttribute @Valid CreatePaySheetDTO paysheet) throws IOException {
        Integer created = paySheetService.uploadFile(paysheet);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
