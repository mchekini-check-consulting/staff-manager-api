package com.example.staffmanagerapi.service;


import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    public CollaboratorService(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    public ResponseEntity add(Collaborator collaborator){
        collaboratorRepository.save(collaborator);
        return new ResponseEntity(collaborator,HttpStatus.CREATED);
    }
}
