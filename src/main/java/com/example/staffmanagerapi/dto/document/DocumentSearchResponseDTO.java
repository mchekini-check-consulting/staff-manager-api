package com.example.staffmanagerapi.dto.document;

import com.example.staffmanagerapi.enums.DocumentTypeEnum;
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
    private Integer id;
    private String collaborator;
    private DocumentTypeEnum type;
    private String name;
    private LocalDate createdAt;
}
