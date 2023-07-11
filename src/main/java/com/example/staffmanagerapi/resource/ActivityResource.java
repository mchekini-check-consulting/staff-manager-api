package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.activity.in.CreateActivityInDto;
import com.example.staffmanagerapi.model.Activity;
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

  @PostMapping
  @Authenticated(authenticated = true, hasAnyRoles = { "collab" })
  public ResponseEntity<Activity> createActivity(
    @RequestBody @Valid CreateActivityInDto data
  ) {
    return ResponseEntity
      .status(HttpStatusCode.valueOf(201))
      .body(this.activityService.createActivity(data));
  }
}
