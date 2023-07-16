package com.example.staffmanagerapi.dto;

import com.example.staffmanagerapi.validators.EmailNotAlreadyExists;
import com.example.staffmanagerapi.validators.NameNotAlreadyExists;
import com.example.staffmanagerapi.validators.PhoneNotAlreadyExists;
import com.example.staffmanagerapi.validators.TvaNumberNotAlreadyExists;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreationDto {
    @Email(message = "Email invalide")
    @NotEmpty(message = "le champ email est obligatoire")
    @EmailNotAlreadyExists(message = "l'email du client existe déjà")
    private String customerEmail;

    @NameNotAlreadyExists
    @NotBlank(message = "Nom du client invalide")
    private String customerName;

    @NotBlank(message = "Addresse invalide")
    private String customerAddress;

    @Pattern(regexp = "^\\d{10}$", message = "Le numéro de téléphone doit contenir 10 chiffres")
    @NotNull(message = "Le numéro de téléphone est obligatoire")
    @PhoneNotAlreadyExists
    private String customerPhone;

    @Pattern(regexp = "^FR\\d{11}$", message = "Numéro TVA non valide")
    @NotNull(message = "Le Numéro TVA est obligatoire")
    @TvaNumberNotAlreadyExists
    private String customerTvaNumber;
}
