package com.example.staffmanagerapi.repository;

import com.example.staffmanagerapi.model.Paysheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaySheetRepository extends JpaRepository<Paysheet, Integer> {
    boolean existsByName(String name);
}
