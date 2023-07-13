package com.example.staffmanagerapi.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;





@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Collaborator {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    @NotBlank(message = "le champ prénom est obligatoire")
    private String firstName;

    @Column(name = "lastname")
    @NotBlank(message = "le champ nom est obligatoire")
    private String lastName;

    @NotBlank(message = "le champ adresse est obligatoire")
    private String address;

    @Email(message = "Entre une adresse mail valide")
    @NotBlank(message = "le champ Email est obligatoire")
    private String email;

    @Size(min = 10,max = 10,message = "le numéro de téléphone doit contenir 10 chiffres")
    @NotBlank(message = "le champ numéro de téléphone est obligatoire")
    private String phone;    
}
