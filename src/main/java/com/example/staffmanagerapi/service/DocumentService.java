package com.example.staffmanagerapi.service;


import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import com.example.staffmanagerapi.utils.DocumentSpecifications;


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

    public Integer uploadFile(CreateDocumentDto dto, User user) throws FileEmptyException, EntityNotFoundException, FileInvalidExtensionException, IOException {
        Collaborator collaborator = this.collaboratorService.findCollaboratorByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Collaborator doesn't exist."));;

        if (dto.getFile().isEmpty()) {
            throw new FileEmptyException("Veuillez séléctionner un fichier valide non vide");
        }

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        String extension = StringUtils.getFilenameExtension(dto.getFile().getOriginalFilename());
        DocumentTypeEnum type = dto.getType();
        String name = dto.getFile().getOriginalFilename();
        MultipartFile content = dto.getFile();
        String bucketName = "justificatifs-check-consulting";
        String createdAt = LocalDate.now().format(formatter);

        String filename = name.substring(0, name.lastIndexOf("."));

        boolean isValidFile = isValidFile(dto.getFile());
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("pdf", "jpg", "jpeg"));


        if (!isValidFile || !allowedFileExtensions.contains(FilenameUtils.getExtension(name))) {
            throw new FileInvalidExtensionException("Type fichier ." + extension + " non autorisé, type de fichiers autorisés: .jpeg, .jpg, .pdf");
        }

       // Uploading file to s3s
        amazonS3Service.upload(content, bucketName, filename.concat(collaborator.getId().toString().concat('.'+extension)));

        // Saving metadata to db
        Document doc = new Document();
        doc.setCollaborator(collaborator);
        doc.setName(filename.concat(collaborator.getId().toString().concat('.'+extension)));
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

    public List<DocumentSearchResponseDTO> getDocumentsWithFilters(List<Long> collaborators, List<DocumentTypeEnum> types){
        Specification<Document> spec = Specification.where(null);

        if (collaborators != null && !collaborators.isEmpty()) {
            spec = spec.and(DocumentSpecifications.withCollaboratorIdsAndTypes(collaborators, types));
        }

        if (types != null && !types.isEmpty()) {
            spec = spec.and(DocumentSpecifications.withTypes(types));
        }

        List<Document> documents = documentRepository.findAll(spec);

        List<DocumentSearchResponseDTO> response =
                documents.stream().map(
                        document -> DocumentSearchResponseDTO.builder()
                        .id(document.getId())
                        .type(document.getType())
                        .name(document.getName())
                        .createdAt(document.getCreatedAt())
                        .collaborator(document.getCollaborator().getFirstName() + " " + document.getCollaborator().getLastName())
                        .build()
                ).toList();

        return response;
    }


}