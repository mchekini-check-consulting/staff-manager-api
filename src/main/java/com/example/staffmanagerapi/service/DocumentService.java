package com.example.staffmanagerapi.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.exception.*;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.DocumentRepository;
import com.example.staffmanagerapi.utils.DocumentSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Objects;

import static com.example.staffmanagerapi.utils.Constants.AUTHORIZED_FILE_EXTENSION;


@Service
@Slf4j
public class DocumentService {


    @Value("${bucket.justificatifs}")
    public String bucketJustificatifName;

    private final AmazonS3Service amazonS3Service;
    private final DocumentRepository documentRepository;
    private final CollaboratorService collaboratorService;

    @Autowired
    public DocumentService(AmazonS3Service amazonS3Service, DocumentRepository documentRepository, CollaboratorService collaboratorService) {
        this.amazonS3Service = amazonS3Service;
        this.documentRepository = documentRepository;
        this.collaboratorService = collaboratorService;
    }

    public Integer uploadFile(CreateDocumentDto dto, User user) throws FileEmptyException, EntityNotFoundException, FileInvalidExtensionException, FileNameExistsException, IOException {
        Collaborator collaborator = this.collaboratorService.findCollaboratorByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Ce collaborateur n'existe pas"));

        if (dto.getFile().isEmpty()) {
            throw new FileEmptyException("Veuillez séléctionner un fichier valide non vide");
        }

        MultipartFile fileContent = dto.getFile();
        String name = fileContent.getOriginalFilename();

        boolean docExists = documentRepository.existsByName(name);
//
        if (docExists) {
            throw new FileNameExistsException("Ce nom de document est déjà existant. Merci de modifier le nom");
        }

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/yyyy HH:mm:ss")
                .withResolverStyle(ResolverStyle.STRICT);

        String extension = StringUtils.getFilenameExtension(name);
        DocumentTypeEnum type = dto.getType();
        String createdAt = LocalDateTime.now().format(formatter);
        boolean isValidFile = isValidFile(fileContent);

        if (!isValidFile || !AUTHORIZED_FILE_EXTENSION.contains(extension)) {
            throw new FileInvalidExtensionException("Type fichier ." + extension + " non autorisé, type de fichiers autorisés: .jpeg, .jpg, .pdf");
        }

        // Uploading file to s3s
        amazonS3Service.upload(fileContent, bucketJustificatifName, name);

        // Saving metadata to db
        Document doc = new Document();
        doc.setCollaborator(collaborator);
        doc.setName(name);
        doc.setType(type);
        doc.setCreatedAt(createdAt);
        return documentRepository.save(doc).getId();
    }

    public byte[] downloadFile(String documentName) {
        if (amazonS3Service.bucketNotExistOrEmpty(bucketJustificatifName)) {
            throw new BadRequestException("la bucket n'existe pas ou est vide");
        }
        try {

            final S3Object s3Object = amazonS3Service.download(bucketJustificatifName, documentName);
            final S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

            byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
            log.info("File downloaded successfully.");
            s3Object.close();
            return content;
        } catch (AmazonS3Exception e) {
            log.error("Error Message= " + e.getMessage());
            throw new NotFoundException(e.getMessage());
        } catch (final Exception ex) {
            log.error("Error Message= " + ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }
    }

    private boolean isValidFile(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename())) return false;
        return !multipartFile.getOriginalFilename().trim().equals("");
    }

    public List<DocumentSearchResponseDTO> getDocumentsWithFilters(List<Long> collaborators, List<DocumentTypeEnum> types) {
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