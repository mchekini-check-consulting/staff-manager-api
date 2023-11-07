package com.example.staffmanagerapi.dto.workCertificate;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkCertificateInDto {

    @Valid
    Long collaboratorId;

}
