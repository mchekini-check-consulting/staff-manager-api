package com.example.staffmanagerapi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class CustomerCreationDto {
    @Email(message = "Email invalide")
    @NotEmpty(message = "Email invalide")
    private String customerEmail;

    @NotBlank(message = "Nom du client invalide")
    private String customerName;

    @NotBlank(message = "Addresse invalide")
    private String customerAddress;

    @Pattern(regexp = "^\\d{10}$", message = "Le numéro de téléphone doit contenir 10 chiffres")
    @NotNull(message = "Le numéro de téléphone doit contenir 10 chiffres")
    private String customerPhone;

    @Pattern(regexp = "^fr\\d{11}$", message = "Numéro TVA non valide")
    @NotNull(message = "Numéro TVA non valide")
    private String customerTvaNumber;
}
