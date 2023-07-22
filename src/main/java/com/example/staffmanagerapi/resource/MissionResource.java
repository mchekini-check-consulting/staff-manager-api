package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.dto.mission.in.MissionDto;
import com.example.staffmanagerapi.model.Mission;
import com.example.staffmanagerapi.service.MissionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping()
    public ResponseEntity getAllMissions(){
        List<MissionDto> missions = missionService.missions();
        return ResponseEntity.status(HttpStatusCode.valueOf(200))
                .body(missions);
    }
}
