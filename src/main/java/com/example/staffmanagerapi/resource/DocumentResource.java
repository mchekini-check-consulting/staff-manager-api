package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.exception.FileNameExistsException;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.service.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/justificatif")
@Slf4j
public class DocumentResource {
    private final DocumentService documentService;
    private final User user;

    public DocumentResource(DocumentService documentService, User user) {
        this.documentService = documentService;
        this.user = user;
    }

    @PostMapping()
    @Authenticated(authenticated = true, hasAnyRoles = {"collab"})
    public ResponseEntity<?> upload(@ModelAttribute @Valid CreateDocumentDto dto) throws FileEmptyException, EntityNotFoundException, FileInvalidExtensionException, FileNameExistsException, IOException {
        this.documentService.uploadFile(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
