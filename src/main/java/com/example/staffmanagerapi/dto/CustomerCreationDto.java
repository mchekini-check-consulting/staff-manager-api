package com.example.staffmanagerapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @Pattern(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w\\w+)(\\s)*$",
            message = "Email non valide")
    private String customerEmail;

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerAddress;

    @Pattern(regexp = "^\\d{10}$", message="Le numero de telephone doit contenir 10 chiffres")
    private String customerPhone;

    @Pattern(regexp = "^fr\\d{11}$", message="Numero TVA non valide")
    private String customerTvaNumber;
}
