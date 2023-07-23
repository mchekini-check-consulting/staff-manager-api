package com.example.staffmanagerapi.dto.document;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentSearchRequestDTO {
    @Valid
    private List<Long> collaborators;
    @Valid
    private List<DocumentTypeEnum> types;
}
