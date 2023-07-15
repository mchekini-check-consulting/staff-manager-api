package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.service.MissionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/mission")
@Slf4j
public class MissionResource {
    private final MissionService missionService;

    public MissionResource(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping()
    public ResponseEntity<?> postMission(@Valid @RequestBody CreateMissionInDto createMissionInDto) {
        log.info("Add mission" + createMissionInDto.getNameMission());
        Integer missionId = missionService.add(createMissionInDto);
        log.info("Mission " + createMissionInDto.getNameMission() + "add success");
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/api/v1/mission/{id}")
                .buildAndExpand(missionId)
                .toUri();
        return ResponseEntity.created(location).build();
    }
}
