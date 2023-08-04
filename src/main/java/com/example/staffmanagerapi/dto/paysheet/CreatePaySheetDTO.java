package com.example.staffmanagerapi.dto.paysheet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaySheetDTO {
    @NotNull(message = "L'Id du collaborateur est obligatoire")
    private Integer collaborator;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$", message = "Veuillez respecter le format suivant : MM/AAAA")
    @NotNull(message = "le mois et l'année de la fiche de paie est obligatoire")
    private String monthYear;

    @NotNull(message = "le fichier à upload est obligatoire")
    private MultipartFile file;

}
