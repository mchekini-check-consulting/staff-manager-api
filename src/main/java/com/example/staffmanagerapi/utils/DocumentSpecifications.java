package com.example.staffmanagerapi.utils;

import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.model.Document;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class DocumentSpecifications {

    public static Specification<Document> withCollaboratorIdsAndTypes(List<Long> collaboratorIds, List<DocumentTypeEnum> types) {
        return (root, query, criteriaBuilder) -> {
            if (collaboratorIds != null && !collaboratorIds.isEmpty()) {
                query.distinct(true);
                return root.get("collaborator").get("id").in(collaboratorIds);
            }
            return null;
        };
    }

    public static Specification<Document> withTypes(List<DocumentTypeEnum> types) {
        return (root, query, criteriaBuilder) -> {
            if (types != null && !types.isEmpty()) {
                query.distinct(true);
                return root.get("type").in(types);
            }
            return null;
        };
    }
}