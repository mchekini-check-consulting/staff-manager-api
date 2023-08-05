package com.example.staffmanagerapi.service;


import com.example.staffmanagerapi.dto.collaborator.CollaboratorDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CollaboratorService(CollaboratorRepository collaboratorRepository, ModelMapper modelMapper) {
        this.collaboratorRepository = collaboratorRepository;
        this.modelMapper = modelMapper;
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

    public CollaboratorDto upDateCollaborator(Long id, CollaboratorDto collaboratorRequest) {

        Collaborator collaborator = collaboratorRepository.findById(id).orElseThrow( ()->new EntityNotFoundException("le collaborateur n'exsite pas"));
        
        String newEmail = collaboratorRequest.getEmail();
        String newPhone = collaboratorRequest.getPhone();

        collaborator.setFirstName(collaboratorRequest.getFirstName());
        collaborator.setLastName(collaboratorRequest.getLastName());


        if(! collaborator.getEmail().equals(newEmail)){
            if( collaboratorRepository.existsByEmail(newEmail)) throw new BadRequestException("l'email existe dèjâ");
            collaborator.setEmail(newEmail);
        }

        if(! collaborator.getPhone().equals(newPhone)){
            if(collaboratorRepository.existsByPhone(newPhone)) throw new BadRequestException("le numéro de téléphone existe dèjâ");
            collaborator.setPhone(newPhone);
        }

        collaborator.setAddress(collaboratorRequest.getAddress());
        Collaborator updatedCollaborator = collaboratorRepository.save(collaborator);
        return modelMapper.map(updatedCollaborator,CollaboratorDto.class);


    }
}
