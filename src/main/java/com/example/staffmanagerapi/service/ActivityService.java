package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.ActivityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CollaboratorService collaboratorService;

    public ActivityService(ActivityRepository activityRepository1, CollaboratorService collaboratorService1) {
        this.activityRepository = activityRepository1;
        this.collaboratorService = collaboratorService1;
    }

    public List<Activity> createActivities(User user, List<ActivityDto> data) {
        Optional<Collaborator> collaborator =
                this.collaboratorService.findCollaboratorByEmail(user.getEmail());

    if (!collaborator.isPresent()) throw new EntityNotFoundException(
      "Collaborator doesn't exist."
    );

    List<Activity> records = data
      .stream()
      .map(row ->
        Activity
          .builder()
          .date(row.getDate())
          .quantity(row.getQuantity())
          .category(row.getCategory())
          .comment(row.getComment())
          .collaborator(collaborator.get())
          .build()
      )
      .toList();

        return this.activityRepository.saveAll(records);
    }
}
