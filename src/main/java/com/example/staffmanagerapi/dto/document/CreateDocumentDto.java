package com.example.staffmanagerapi.dto.document;


import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDocumentDto {
    @NotNull(message = "type du document est obligatoire")
    private DocumentTypeEnum type;

    @NotNull(message = "le fichier à upload est obligatoire")
    private MultipartFile file;
}
