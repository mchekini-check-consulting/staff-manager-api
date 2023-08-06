package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.model.Paysheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaySheetRepository extends JpaRepository<Paysheet, Integer>, JpaSpecificationExecutor<Paysheet> {
    boolean existsByName(String name);
    boolean existsByCollaboratorAndMonthYear(Collaborator collaborator, String monthYear);
    Optional<Paysheet> findByName(String name);
}
