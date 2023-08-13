package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.dto.mission.in.MissionDto;
import com.example.staffmanagerapi.dto.mission.in.UpdateMissionInDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.exception.UpdateArgumentsNonValid;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.model.Mission;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.repository.MissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MissionService {
    private final MissionRepository missionRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public MissionService(MissionRepository missionRepository, CollaboratorRepository collaboratorRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.missionRepository = missionRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public Integer add(CreateMissionInDto missionInDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        TypeMap<CreateMissionInDto, Mission> typeMap = modelMapper.getTypeMap(CreateMissionInDto.class, Mission.class);
        if (typeMap == null) { // if not  already added
            modelMapper.addMappings(new PropertyMap<CreateMissionInDto, Mission>() {
                @Override
                protected void configure() {
                    skip(destination.getId());
                    skip(destination.getCollaborator());
                    skip(destination.getCustomer());
                }
            });
        }

        Mission mission = modelMapper.map(missionInDto, Mission.class);
        if(Objects.nonNull(missionInDto.getCollaboratorId())){
            mission.setCollaborator(collaboratorRepository.getReferenceById(missionInDto.getCollaboratorId()));
        }
        mission.setCustomer(customerRepository.getReferenceById(missionInDto.getCustomerId()));
        mission.setStartingDateMission(LocalDate.parse(missionInDto.getStartingDateMission(), formatter));
        mission.setEndingDateMission(LocalDate.parse(missionInDto.getEndingDateMission(), formatter));

        return missionRepository.save(mission).getId();

    }

    public List<MissionDto> missions(){
        return missionRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<Mission> getCollaboratorMissions(Collaborator collaborator) {
        Specification<Mission> specification = Specification.where(this.filterByCollaborator(collaborator));
        return this.missionRepository.findAll(specification); 
    }

    @Transactional
    public void updateMission(Integer missionId, UpdateMissionInDto updateMissionDto) {
        Map<String,String> errors = new HashMap<>();

        Optional<Mission> existByName = missionRepository.findByNameMissionAndIdIsNot(updateMissionDto.getNameMission(),missionId);
        Optional<Mission> isMissionExist = missionRepository.findById(missionId);
        if(isMissionExist.isEmpty()){
            errors.put("missionId","Mission avecc L'id "+missionId+ " +est introuvable");
        }
        if(existByName.isPresent()){
            errors.put("nameMission","Le nom de la mission existe déjà");
        }

        if(!errors.isEmpty()){
            throw  new UpdateArgumentsNonValid("Les informations ne sont pas fiable", errors);
        }

        Mission mission = isMissionExist.get();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        mission.setNameMission(updateMissionDto.getNameMission());
        mission.setStartingDateMission(LocalDate.parse(updateMissionDto.getStartingDateMission(), formatter));
        mission.setEndingDateMission(LocalDate.parse(updateMissionDto.getEndingDateMission(), formatter));

        if (Objects.nonNull(updateMissionDto.getCollaboratorId())) {
            Collaborator collaborator = collaboratorRepository.getReferenceById(updateMissionDto.getCollaboratorId());
            mission.setCollaborator(collaborator);
            log.info("La mission a été affectée au collaborateur avec l'identifiant : " + updateMissionDto.getCollaboratorId());
        }

        Customer customer = customerRepository.getReferenceById(updateMissionDto.getCustomerId());
        mission.setCustomer(customer);

        missionRepository.save(mission);
        log.info("La mission avec l'identifiant " + missionId + " a été mise à jour");
    }

    private MissionDto convertEntityToDto(Mission mission){
        modelMapper.
                typeMap(Mission.class, MissionDto.class).
                addMapping(Mission::getNameMission , MissionDto::setMissionName)
                .addMappings(mapper-> mapper.map(src->src.getCustomer().getCustomerName(),MissionDto::setCustomerName))
                .addMappings(mapper-> mapper.map(src->src.getCollaborator().getFirstName(),MissionDto::setFirstName))
                .addMappings(mapper-> mapper.map(src->src.getCollaborator().getLastName(),MissionDto::setLastName));

        return modelMapper.map(mission, MissionDto.class);
    }

    private Specification<Mission> filterByCollaborator(Collaborator collaborator) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("collaborator"), collaborator);
        };
    }
}
