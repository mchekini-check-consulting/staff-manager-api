package com.example.staffmanagerapi.resource;


import com.example.staffmanagerapi.dto.workCertificate.WorkCertificateInDto;
import com.example.staffmanagerapi.service.WorkCertificateService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/workCertificate")
public class WorkCertificateResource {

    private final WorkCertificateService workCertificateService;

    public WorkCertificateResource(WorkCertificateService workCertificateService) {
        this.workCertificateService = workCertificateService;
    }

    @PostMapping
    public ResponseEntity<byte[]> generateWorkCertificate(@RequestBody @Valid WorkCertificateInDto workCertificateInDto) throws Exception {
        log.info("collaborator sent to generateWorkCertificateResource is: {} ", workCertificateInDto);
        return ResponseEntity.ok(workCertificateService.generateNewCertificate(workCertificateInDto.getCollaboratorId()));
    }
}
