package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.dto.mission.in.MissionDto;
import com.example.staffmanagerapi.mapper.MissionMapper;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Mission;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.repository.MissionRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MissionService {

  private final MissionRepository missionRepository;
  private final CollaboratorRepository collaboratorRepository;
  private final CustomerRepository customerRepository;
  private final MissionMapper missionMapper;

  public MissionService(
    MissionRepository missionRepository,
    CollaboratorRepository collaboratorRepository,
    CustomerRepository customerRepository,
    MissionMapper missionMapper
  ) {
    this.missionRepository = missionRepository;
    this.collaboratorRepository = collaboratorRepository;
    this.customerRepository = customerRepository;
    this.missionMapper = missionMapper;
  }

  public Integer add(CreateMissionInDto missionInDto) {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE
      .ofPattern("dd/MM/uuuu")
      .withResolverStyle(ResolverStyle.STRICT);

    Mission mission =
      this.missionMapper.CreateMissionInDtoToMission(missionInDto);
    if (Objects.nonNull(missionInDto.getCollaboratorId())) {
      mission.setCollaborator(
        collaboratorRepository.getReferenceById(
          missionInDto.getCollaboratorId()
        )
      );
    }
    mission.setCustomer(
      customerRepository.getReferenceById(missionInDto.getCustomerId())
    );
    mission.setStartingDateMission(
      LocalDate.parse(missionInDto.getStartingDateMission(), formatter)
    );
    mission.setEndingDateMission(
      LocalDate.parse(missionInDto.getEndingDateMission(), formatter)
    );

    return missionRepository.save(mission).getId();
  }

  public List<MissionDto> missions() {
    return missionRepository
      .findAll()
      .stream()
      .map(this::convertEntityToDto)
      .collect(Collectors.toList());
  }

  public List<Mission> getCollaboratorMissions(Collaborator collaborator) {
    Specification<Mission> specification = Specification.where(
      this.filterByCollaborator(collaborator)
    );
    return this.missionRepository.findAll(specification);
  }

  private MissionDto convertEntityToDto(Mission mission) {
    return this.missionMapper.missionToMissionDto(mission);
  }

  private Specification<Mission> filterByCollaborator(
    Collaborator collaborator
  ) {
    return (root, query, criteriaBuilder) -> {
      return criteriaBuilder.equal(root.get("collaborator"), collaborator);
    };
  }
}
