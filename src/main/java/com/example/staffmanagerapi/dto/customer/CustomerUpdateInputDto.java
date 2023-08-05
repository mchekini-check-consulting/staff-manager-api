package com.example.staffmanagerapi.dto.customer;
import com.example.staffmanagerapi.validators.customer.CustomerIdNotExist;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateInputDto {
    @NotNull(message = "L'id du client est requis")
    @CustomerIdNotExist
    private Long id;

    @Email(message = "Email invalide")
    @NotEmpty(message = "le champ email est obligatoire")
    //@UpdateCustomerEmailExist
    private String customerEmail;


    @NotBlank(message = "Nom du client invalide")
    private String customerName;

    @NotBlank(message = "Addresse invalide")
    private String customerAddress;

    @Pattern(regexp = "^\\d{10}$", message = "Le numéro de téléphone doit contenir 10 chiffres")
    @NotNull(message = "Le numéro de téléphone est obligatoire")
    private String customerPhone;

    @Pattern(regexp = "^FR\\d{11}$", message = "Numéro TVA non valide")
    @NotNull(message = "Le Numéro TVA est obligatoire")
    private String customerTvaNumber;
}
