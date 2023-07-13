package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.activity.in.CreateActivityInDto;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/activity")
public class ActivityResource {

  @Autowired
  private ActivityService activityService;

  public ActivityResource(ActivityService ActivityService) {}

  @Autowired
  private User user;

  @PostMapping
  @Authenticated(authenticated = true, hasAnyRoles = { "collab" })
  public ResponseEntity<?> createActivities(
    @RequestBody @Valid CreateActivityInDto data
  ) {
    this.activityService.createActivities(user, data.getActivities());
    return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
  }
}
