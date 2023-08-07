package com.example.staffmanagerapi.resource;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.paysheet.CreatePaySheetDTO;
import com.example.staffmanagerapi.dto.paysheet.SearchPaySheetDTO;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.exception.FileNameDoesNotExistException;
import com.example.staffmanagerapi.model.Paysheet;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.service.PaySheetService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/paysheet")
@Slf4j
public class PaySheetResource {

    private final PaySheetService paySheetService;
    private final User user;

    public PaySheetResource(PaySheetService paySheetService, User user) {
        this.paySheetService = paySheetService;
        this.user = user;
    }

    @PostMapping()
    public ResponseEntity<?> upload(@ModelAttribute @Valid CreatePaySheetDTO paysheet) throws IOException {
        Integer created = paySheetService.uploadFile(paysheet);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/search")
    public ResponseEntity<?> recherche(@RequestBody @Valid SearchPaySheetDTO searchPaySheetDTO) throws IOException {
        try {
            log.info("Tentative de recherche des fiches de paie du collaborateur {} ... ", user.getEmail());
            List<Paysheet> response = paySheetService.search(searchPaySheetDTO, user);
            log.info("Recherche des fiches de paie du collaborateur {} effectué avec success, n°fiches : {} ", user.getEmail(), response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Erreur lors de la recherches des fiches de paie de l'utilisateur {}", user.getEmail());
            log.error("{}", e.getMessage());
            throw new RuntimeException();
        }
    }


    @GetMapping("/{fileName}")
    @Authenticated(authenticated = true, hasAnyRoles = {"collab"})
    public ResponseEntity<?> download(@PathVariable("fileName") String fileName) throws IOException, AmazonS3Exception, BadRequestException, FileNameDoesNotExistException {
        Object object = paySheetService.downloadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(object);
    }
}