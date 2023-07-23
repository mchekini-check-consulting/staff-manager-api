package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.ActivityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public void getCurrentMonthCRA() throws JsonProcessingException {
        log.info("Attempting to fetch compte-rendu-activité for current month : {} ...", LocalDate.now().getMonth());
        // constants
        YearMonth currentMonth = YearMonth.now();
        // processing activities

        Map<Collaborator,List<Activity>> activities = this.activityRepository.findAll()
                .stream()
                .filter(activity -> activity.getCollaborator() != null ) // remove activities with no collaborator
                .filter( activity -> YearMonth.from(activity.getDate()).equals(currentMonth)) // filter to current month only
                .collect(Collectors.groupingBy(Activity::getCollaborator));


        activities.forEach((collaborator, activityList) -> {
            log.info("Collaborator: {}", collaborator.getFirstName() + collaborator.getLastName());
            log.info("Activities: {}", activityList);
            log.info("Number of activities: {} ", activityList.size());
        });

    }

}
