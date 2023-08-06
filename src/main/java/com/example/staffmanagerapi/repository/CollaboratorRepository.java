package com.example.staffmanagerapi.repository;


import com.example.staffmanagerapi.model.Collaborator;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator,Long> {
    Optional<Collaborator> findByEmail(String email);

    boolean existsByEmail(String Email);
    boolean existsByPhone(String Phone);
}
