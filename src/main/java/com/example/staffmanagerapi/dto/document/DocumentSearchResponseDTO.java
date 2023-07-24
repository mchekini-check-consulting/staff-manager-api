package com.example.staffmanagerapi.dto.document;

import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSearchResponseDTO {
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @NotNull(message = "Collaborator cannot be null")
    private String collaborator;

    @NotNull(message = "Type cannot be null")
    private DocumentTypeEnum type;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "CreatedAt cannot be null")
    private LocalDate createdAt;
}
