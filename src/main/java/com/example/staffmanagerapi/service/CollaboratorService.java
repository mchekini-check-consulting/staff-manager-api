package com.example.staffmanagerapi.service;


import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.CollaboratorRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    public CollaboratorService(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    public Collaborator add(Collaborator collaborator){
        collaboratorRepository.save(collaborator);
        return collaborator;
    }

    public Optional<Collaborator> findCollaboratorByEmail(String email){
        return collaboratorRepository.findByEmail(email);
    }

    public List<Collaborator> getAllCollaborators() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return collaboratorRepository.findAll(sort);
    }
}
