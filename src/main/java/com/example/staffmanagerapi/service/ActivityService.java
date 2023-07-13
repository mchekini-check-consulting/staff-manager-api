package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.ActivityRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

  @Autowired
  private ActivityRepository activityRepository;

  @Autowired
  private CollaboratorService collaboratorService;

  public List<Activity> createActivities(User user, List<ActivityDto> data) {
    Collaborator collaborator =
      this.collaboratorService.findCollaboratorByEmail(user.getEmail());

    if (collaborator == null) throw new EntityNotFoundException(
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
          .collaborator(collaborator)
          .build()
      )
      .toList();

    return this.activityRepository.saveAll(records);
  }
}
