package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.activity.in.CreateActivityInDto;
import com.example.staffmanagerapi.dto.activity.out.CompteRenduActiviteOutDto;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/activity")
public class ActivityResource {


    private final ActivityService activityService;
    private final User user;

    public ActivityResource(ActivityService activityService, User user) {
        this.activityService = activityService;
        this.user = user;
    }

    @GetMapping
    @Authenticated(authenticated = true, hasAnyRoles = {"admin"})
    public ResponseEntity<List<CompteRenduActiviteOutDto>> getCurrentMonthCRA() {
        return ResponseEntity.ok(this.activityService.getCurrentMonthCRA());
    }

    @PostMapping
    // @Authenticated(authenticated = true, hasAnyRoles = {"collab"})
    public ResponseEntity<?> createActivities(
            @RequestBody @Valid CreateActivityInDto data
    ) {
        System.out.println("Hey"+ user.getEmail());
        this.activityService.createActivities(user, data.getActivities());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }
}
