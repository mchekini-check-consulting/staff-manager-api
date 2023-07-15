package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.model.Mission;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.repository.MissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

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


        // TODO create an annotation to validate the date Fields Format
        LocalDate startingDate;
        LocalDate endingDate;
        try {
            // JAVA LOCALDATE USES uuuu FOR YEARS AND YYYY for ERA
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            startingDate = LocalDate.parse(missionInDto.getStartingDateMission(), formatter);
            endingDate = LocalDate.parse(missionInDto.getEndingDateMission(), formatter);
        } catch (DateTimeException e) {
            log.warn(e.getMessage());
            throw new BadRequestException("Invalid date");
        }

        modelMapper.addMappings(new PropertyMap<CreateMissionInDto, Mission>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        Mission mission = modelMapper.map(missionInDto, Mission.class);
        mission.setCollaborator(collaboratorRepository.getReferenceById(missionInDto.getCollaboratorId()));
        mission.setCustomer(customerRepository.getReferenceById(missionInDto.getCustomerId()));
        mission.setStartingDateMission(startingDate);
        mission.setEndingDateMission(endingDate);

        return missionRepository.save(mission).getId();

    }
}
