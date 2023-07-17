package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.model.Mission;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.repository.MissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Objects;

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
}
