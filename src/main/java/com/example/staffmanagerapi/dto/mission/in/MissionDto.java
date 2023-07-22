package com.example.staffmanagerapi.dto.mission.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({
        "id",
        "missionName",
        "startingDate",
        "endingDate",
        "customerName",
        "customerContactLastname",
        "customerContactFirstname",
        "customerContactEmail",
        "customerContactPhone",
        "collaboratorFirstName",
        "collaboratorLastName",
        "missionDescription"

})
public class MissionDto {
    private Long id;
    private String missionName;
    @JsonProperty("startingDate")
    private LocalDate startingDateMission;
    @JsonProperty("endingDate")
    private LocalDate endingDateMission;
    private String customerName;
    @JsonProperty("collaboratorFirstName")
    private String firstName;
    @JsonProperty("collaboratorLastName")
    private String lastName;
    @JsonProperty("customerContactLastName")
    private String customerContactLastname;
    @JsonProperty("customerContactFirstName")
    private String customerContactFirstname;
    private String customerContactEmail;
    private String customerContactPhone;
    private String missionDescription;

}
