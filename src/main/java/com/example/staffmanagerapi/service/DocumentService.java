package com.example.staffmanagerapi.service;


import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.exception.FileNameExistsException;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
public class DocumentService {
    private final AmazonS3Service amazonS3Service;
    private final DocumentRepository documentRepository;
    private final CollaboratorService collaboratorService;

    @Autowired
    public DocumentService(AmazonS3Service amazonS3Service, DocumentRepository documentRepository, CollaboratorService collaboratorService) {
        this.amazonS3Service = amazonS3Service;
        this.documentRepository = documentRepository;
        this.collaboratorService = collaboratorService;
    }

    public Integer uploadFile(CreateDocumentDto dto, User user) throws FileEmptyException, EntityNotFoundException, FileNameExistsException, FileInvalidExtensionException, IOException {
        Collaborator collaborator = this.collaboratorService.findCollaboratorByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Ce collaborateur n'existes pas"));;

        if (dto.getFile().isEmpty()) {
            throw new FileEmptyException("Veuillez séléctionner un fichier valide non vide");
        }

        MultipartFile fileContent = dto.getFile();

        String fileName = fileContent.getOriginalFilename();
        boolean docExists = documentRepository.findByName(fileName);

        if (docExists) {
            throw new FileNameExistsException("Ce nom de document est déjà existant. Merci de modifier le nom");
        }

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")
                .withResolverStyle(ResolverStyle.STRICT);

        String extension = StringUtils.getFilenameExtension(fileContent.getOriginalFilename());
        DocumentTypeEnum type = dto.getType();

        String bucketName = "justificatifs-check-consulting";
        String createdAt = LocalDateTime.now().format(formatter);

        boolean isValidFile = isValidFile(fileContent);
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("pdf", "jpg", "jpeg"));


        if (!isValidFile || !allowedFileExtensions.contains(FilenameUtils.getExtension(fileName))) {
            throw new FileInvalidExtensionException("Type fichier ." + extension + " non autorisé, type de fichiers autorisés: .jpeg, .jpg, .pdf");
        }

       // Uploading file to s3s
        amazonS3Service.upload(fileContent, bucketName, fileName);

        // Saving metadata to db
        Document doc = new Document();
        doc.setCollaborator(collaborator);
        doc.setName(fileName);
        doc.setType(type);
        doc.setCreatedAt(createdAt);
        return documentRepository.save(doc).getId();
    }


    private boolean isValidFile(MultipartFile multipartFile){
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }
}