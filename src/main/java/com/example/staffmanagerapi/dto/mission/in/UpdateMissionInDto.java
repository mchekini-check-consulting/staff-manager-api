package com.example.staffmanagerapi.dto.mission.in;

import com.example.staffmanagerapi.utils.Constants;
import com.example.staffmanagerapi.validators.collaborator.CollaboratorNotExists;
import com.example.staffmanagerapi.validators.customer.CustomerNotExists;
import com.example.staffmanagerapi.validators.mission.MissionDateFormatIncorrect;
import com.example.staffmanagerapi.validators.mission.MissionNotExists;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateMissionInDto {

    @NotBlank(message = "champ obligatoire")
    private String nameMission;
    @NotBlank(message = "champ obligatoire")
    @Pattern(regexp = Constants.DATE_REGEX, message = "Date invalide")
    @MissionDateFormatIncorrect
    private String startingDateMission;
    @NotBlank(message = "champ obligatoire")
    @Pattern(regexp = Constants.DATE_REGEX, message = "Date invalide")
    @MissionDateFormatIncorrect
    private String endingDateMission;

    @CollaboratorNotExists
    private Long collaboratorId;

    @NotNull(message = "champ obligatoire")
    @CustomerNotExists
    private Long customerId;

    @NotBlank(message = "champ obligatoire")
    private String customerContactLastname;
    @NotBlank(message = "champ obligatoire")
    private String customerContactFirstname;
    @NotBlank(message = "champ obligatoire")
    @Email
    private String customerContactEmail;
    @NotBlank(message = "champ obligatoire")
    @Pattern(regexp = "^\\d{10}$", message = "Numéro invalide")
    private String customerContactPhone;
    private String missionDescription;
}
