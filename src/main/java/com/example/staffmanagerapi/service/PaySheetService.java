package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.paysheet.CreatePaySheetDTO;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.exception.FileNameExistsException;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Paysheet;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.PaySheetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;


@Service
public class PaySheetService {

    @Value("${bucket.fiche-de-paie}")
    private String paysheetBucket;
    private final PaySheetRepository paySheetRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final AmazonS3Service amazonS3Service;

    public PaySheetService(PaySheetRepository paySheetRepository, CollaboratorRepository collaboratorRepository, AmazonS3Service amazonS3Service) {
        this.paySheetRepository = paySheetRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.amazonS3Service = amazonS3Service;
    }

    public Integer uploadFile(CreatePaySheetDTO paySheetDTO) throws FileEmptyException, EntityNotFoundException, FileInvalidExtensionException, FileNameExistsException, IOException {
        Collaborator collaborator = collaboratorRepository.findById(paySheetDTO.getCollaborator().longValue())
                .orElseThrow(()-> new EntityNotFoundException("Ce collaborateur n'existe pas"));

        if(paySheetDTO.getFile().isEmpty()){
            throw new FileEmptyException("Veuillez séléctionner un fichier valide non vide");
        }

        boolean isAttributed = paySheetRepository.existsByCollaboratorAndMonthYear(collaborator, paySheetDTO.getMonthYear());

        if(isAttributed){
            throw new FileNameExistsException("Fiche de paie déjà attribuée");
        }

        MultipartFile fileContent = paySheetDTO.getFile();
        String name = fileContent.getOriginalFilename();

        boolean docExists = paySheetRepository.existsByName(name);

        if (docExists) {
            throw new FileNameExistsException("Ce nom de document est déjà existant. Merci de modifier le nom");
        }

        String extension = StringUtils.getFilenameExtension(name);
        boolean isValidFile = isValidFile(fileContent);

        if (!isValidFile || !(Objects.equals(extension, "pdf"))) {
            throw new FileInvalidExtensionException("Type fichier ." + extension + " non autorisé, Veuillez choisir un fichier .pdf");
        }

        // Uploading file to s3s
        amazonS3Service.upload(fileContent, paysheetBucket, name);

        Paysheet paysheet = Paysheet.builder()
                .collaborator(collaborator)
                .monthYear(paySheetDTO.getMonthYear())
                .name(name)
                .build();

        return paySheetRepository.save(paysheet).getId();
    }

    private boolean isValidFile(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename())) return false;
        return !multipartFile.getOriginalFilename().trim().equals("");
    }
}
