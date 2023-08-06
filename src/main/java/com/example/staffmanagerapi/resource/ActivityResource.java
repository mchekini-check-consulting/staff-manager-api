package com.example.staffmanagerapi.resource;

import static org.springframework.http.HttpStatus.CREATED;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.activity.in.CreateActivityInDto;
import com.example.staffmanagerapi.dto.activity.out.CompteRenduActiviteOutDto;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.service.ActivityService;
import jakarta.validation.Valid;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/activity")
@Slf4j
public class ActivityResource {

  private final ActivityService activityService;
  private final User user;

  public ActivityResource(ActivityService activityService, User user) {
    this.activityService = activityService;
    this.user = user;
  }

  @GetMapping
  @Authenticated(authenticated = true, hasAnyRoles = { "admin" })
  public ResponseEntity<List<CompteRenduActiviteOutDto>> getCurrentMonthCRA() {
    return ResponseEntity.ok(this.activityService.getCurrentMonthCRA());
  }

  @PostMapping
  @ResponseStatus(CREATED)
  @Authenticated(authenticated = true, hasAnyRoles = {"collab"})
  public void createActivities(@RequestBody @Valid CreateActivityInDto data) {
    this.activityService.createActivities(user, data.getActivities());
  }

  @PostMapping("/validate-cra/{collaboratorId}")
  //@Authenticated(authenticated = true, hasAnyRoles = { "admin" })
  @ResponseStatus(CREATED)
  public void validerCRA(@PathVariable Long collaboratorId) {
    log.info("Tentative de validation du CRA pour le collaborateur possedant l'ID : {} ... ",collaboratorId);
    this.activityService.validerCRA(collaboratorId);
  }

}
