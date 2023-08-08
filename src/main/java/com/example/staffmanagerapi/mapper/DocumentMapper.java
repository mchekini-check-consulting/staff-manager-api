package com.example.staffmanagerapi.mapper;

import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
  @Mapping(
    target = "collaborator",
    expression = "java(document.getCollaborator().getFirstName() + ' ' + document.getCollaborator().getLastName())"
  )
  DocumentSearchResponseDTO documentToDocumentSearchResponseDTO(
    Document document
  );
}
