package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer>, JpaSpecificationExecutor<Document> {
}