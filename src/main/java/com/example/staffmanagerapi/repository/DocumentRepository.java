package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer>, JpaSpecificationExecutor<Document> {
    boolean existsByName(String name);
    Optional<Document> findByName(String nom);
}
