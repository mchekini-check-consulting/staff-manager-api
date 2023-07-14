package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.service.CollaboratorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/collaborator")
@Slf4j
public class CollaboratorResource {

    private final CollaboratorService collaboratorService;

    @Autowired
    public CollaboratorResource(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @PostMapping("")
    public ResponseEntity addCollaborator(@Valid @RequestBody Collaborator collaborator) {
        HttpHeaders responseHeaders = new HttpHeaders();

        log.info("Ajout du collaborateur {}", collaborator.getEmail());

        collaboratorService.add(collaborator);

        log.info("Le collaborateur avec l'email {} est renregistré avec succès", collaborator.getEmail());

        responseHeaders.set("Location", "api/v1/collaborator/" + collaborator.getId().toString());

        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .headers(responseHeaders)
                .body(collaborator);
    }


}
