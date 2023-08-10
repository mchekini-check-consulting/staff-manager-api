package com.example.staffmanagerapi.mapper;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.dto.mission.in.MissionDto;
import com.example.staffmanagerapi.model.Mission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MissionMapper {
  @Mapping(
    target = "customerName",
    expression = "java(document.getCustomer().getCustomerName)"
  )
  @Mapping(
    target = "firstName",
    expression = "java(document.getCollaborator().getFirstName()"
  )
  @Mapping(
    target = "lastName",
    expression = "java(document.getCollaborator().getLastName()"
  )
  @Mapping(source = "nameMission", target = "missionName")
  MissionDto missionToMissionDto(Mission mission);

  Mission CreateMissionInDtoToMission(CreateMissionInDto mission);
}
