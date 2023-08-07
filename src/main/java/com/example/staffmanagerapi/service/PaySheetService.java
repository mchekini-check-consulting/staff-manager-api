package com.example.staffmanagerapi.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.staffmanagerapi.dto.paysheet.CreatePaySheetDTO;
import com.example.staffmanagerapi.dto.paysheet.SearchPaySheetDTO;
import com.example.staffmanagerapi.exception.*;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Paysheet;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.PaySheetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class PaySheetService {

    private final PaySheetRepository paySheetRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final AmazonS3Service amazonS3Service;
    @Value("${bucket.fiche-de-paie}")
    private String bucketPaysheet;

    public PaySheetService(PaySheetRepository paySheetRepository, CollaboratorRepository collaboratorRepository, AmazonS3Service amazonS3Service) {
        this.paySheetRepository = paySheetRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.amazonS3Service = amazonS3Service;
    }

    public Integer uploadFile(CreatePaySheetDTO paySheetDTO) throws FileEmptyException, EntityNotFoundException, FileInvalidExtensionException, FileNameExistsException, IOException {
        Collaborator collaborator = collaboratorRepository.findById(paySheetDTO.getCollaborator().longValue())
                .orElseThrow(() -> new EntityNotFoundException("Ce collaborateur n'existe pas"));

        if (paySheetDTO.getFile().isEmpty()) {
            throw new FileEmptyException("Veuillez séléctionner un fichier valide non vide");
        }

        boolean isAttributed = paySheetRepository.existsByCollaboratorAndMonthYear(collaborator, paySheetDTO.getMonthYear());

        if (isAttributed) {
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
        amazonS3Service.upload(fileContent, bucketPaysheet, name);

        Paysheet paysheet = Paysheet.builder()
                .collaborator(collaborator)
                .monthYear(paySheetDTO.getMonthYear())
                .name(name)
                .build();

        return paySheetRepository.save(paysheet).getId();
    }

    public List<Paysheet> search(SearchPaySheetDTO searchPaySheetDTO, User user) {

        Optional<Collaborator> collaboratorOptional = this.collaboratorRepository.findByEmail(user.getEmail());
        if (collaboratorOptional.isEmpty()) {
            throw new EntityNotFoundException("Le Collaborateur qui possede l'email " + user.getEmail() + " n'existe pas!");
        }
        Collaborator collaborator = collaboratorOptional.get();

        Specification<Paysheet> specification = Specification
                .where(this.filterByStartInterval(searchPaySheetDTO))
                .and(this.filterByEndInterval(searchPaySheetDTO))
                .and(this.filterByCollaborator(collaborator));

        Sort sortOptions = Sort.by(Sort.Direction.DESC, "monthYear");
        return this.paySheetRepository.findAll(specification, sortOptions);
    }

    private Specification<Paysheet> filterByStartInterval(SearchPaySheetDTO searchPaySheetDTO) {
        if (searchPaySheetDTO.getStartDate() != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("monthYear"), searchPaySheetDTO.getStartDate());
            };
        }
        return null;
    }

    private Specification<Paysheet> filterByEndInterval(SearchPaySheetDTO searchPaySheetDTO) {
        if (searchPaySheetDTO.getEndDate() != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.lessThanOrEqualTo(root.get("monthYear"), searchPaySheetDTO.getEndDate());
            };
        }
        return null;
    }

    private Specification<Paysheet> filterByCollaborator(Collaborator collaborator) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("collaborator"), collaborator);
        };
    }

    public byte[] downloadFile(String fileName) throws IOException, AmazonS3Exception, BadRequestException, FileNameDoesNotExistException  {
        if (amazonS3Service.bucketNotExistOrEmpty(bucketPaysheet)) {
            throw new BadRequestException("La bucket n'existe pas ou est vide");
        }
        if (!amazonS3Service.doesFileExists(bucketPaysheet, fileName)) {
            throw new FileNameDoesNotExistException("Cette fiche de paie n'existe pas");
        }

        final S3Object s3Object = amazonS3Service.download(bucketPaysheet, fileName);
        final S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

        byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
        s3Object.close();
        return content;


//        if (amazonS3Service.bucketNotExistOrEmpty(bucketPaysheet)) {
//            throw new BadRequestException("la bucket n'existe pas ou est vide");
//        }
//        try {
//
//            final S3Object s3Object = amazonS3Service.download(bucketPaysheet, fileName);
//            final S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
//
//            byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
//            log.info("File downloaded successfully.");
//            s3Object.close();
//            return content;
//        } catch (AmazonS3Exception e) {
//            log.error("Error Message= " + e.getMessage());
//            throw new NotFoundException(e.getMessage());
//        } catch (final Exception ex) {
//            log.error("Error Message= " + ex.getMessage());
//            throw new BadRequestException(ex.getMessage());
//        }
    }

    private boolean isValidFile(MultipartFile multipartFile) {
        if (Objects.isNull(multipartFile.getOriginalFilename())) return false;
        return !multipartFile.getOriginalFilename().trim().equals("");
    }

}
