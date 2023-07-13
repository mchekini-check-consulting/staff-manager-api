package com.example.staffmanagerapi.repository;


import com.example.staffmanagerapi.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator,Long> {
}
