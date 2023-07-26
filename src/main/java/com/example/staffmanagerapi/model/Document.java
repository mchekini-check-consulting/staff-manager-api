package com.example.staffmanagerapi.model;

import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Document {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "name")
        private String name;

        @Column(name = "type")
        @Enumerated(STRING)
        private DocumentTypeEnum type;

        @ManyToOne
        @JoinColumn(name = "collaborator_id")
        private Collaborator collaborator;

        @Column(name = "createdAt")
        private String createdAt;


}

