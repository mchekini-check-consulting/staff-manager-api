package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.dto.document.DocumentSearchRequestDTO;
import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.service.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import java.io.IOException;

@RestController
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
    public ResponseEntity<?> upload(@ModelAttribute @Valid CreateDocumentDto dto) throws FileEmptyException, EntityNotFoundException, FileInvalidExtensionException, IOException {
        this.documentService.uploadFile(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("search")
    @Authenticated(authenticated = true, hasAnyRoles = {"admin"})
    public ResponseEntity search(@Valid @RequestBody DocumentSearchRequestDTO searchRequest){
        List<Long> collaboratorIds = searchRequest.getCollaborators();
        List<DocumentTypeEnum> types = searchRequest.getTypes();
        List<DocumentSearchResponseDTO> documents = documentService.getDocumentsWithFilters(collaboratorIds, types);
        return ResponseEntity.status(HttpStatus.OK).body(documents);
    }

}
