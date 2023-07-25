package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    boolean findByName(String name);
}
