package com.example.staffmanagerapi.dto.collaborator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollaboratorDto {

    private long id;

    @NotBlank(message = "le champ prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "le champ nom est obligatoire")
    private String lastName;

    @NotBlank(message = "le champ adresse est obligatoire")
    private String address;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Entrer une adresse Email valide")
    @NotBlank(message = "le champ Email est obligatoire")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Le numéro de téléphone doit contenir 10 chiffres")
    @NotBlank(message = "le champ numéro de téléphone est obligatoire")
    private String phone;
}
