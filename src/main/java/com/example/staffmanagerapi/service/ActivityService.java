package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import com.example.staffmanagerapi.dto.activity.out.CompteRenduActiviteOutDto;
import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.ActivityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.staffmanagerapi.utils.Constants.*;

@Service
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CollaboratorService collaboratorService;

    public ActivityService(ActivityRepository activityRepository1, CollaboratorService collaboratorService1) {
        this.activityRepository = activityRepository1;
        this.collaboratorService = collaboratorService1;
    }

    public static Double sumDaysByCategory(List<Activity> activities, Set<ActivityCategoryEnum> categories) {
        return hoursToDays(
                activities.stream()
                        .filter(activity -> categories.stream().anyMatch(cat -> activity.getCategory() == cat))
                        .mapToInt(Activity::getQuantity)
                        .sum()
        );
    }

    public static Double hoursToDays(Integer hours) {
        return (double) (hours / HOURS_TO_DAY_RATIO);
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

    public List<CompteRenduActiviteOutDto> getCurrentMonthCRA() throws JsonProcessingException {

        // constants
        YearMonth currentMonth = YearMonth.now();
        log.info("Attempting to fetch compte-rendu-activité for current month : {} ...", currentMonth);

        // traitement activitiés
        Map<Collaborator, List<Activity>> activities = this.activityRepository.findAll()
                .stream()
                .filter(activity -> activity.getCollaborator() != null) // remove activities with no collaborator
                .filter(activity -> YearMonth.from(activity.getDate()).equals(currentMonth)) // filter to current month only
                .collect(Collectors.groupingBy(Activity::getCollaborator)); // turn List into a Map

        // une fois groupé par collaborateur dans un Map<Collaborator,List<Activity>>
        // on map sur un DTO List<CompteRenduActiviteOutDto>

        List<CompteRenduActiviteOutDto> cra = new ArrayList<>();

        activities.forEach((collaborator, activityList) -> {

            log.debug("Processing Collaborator: {}", collaborator.getFirstName() + collaborator.getLastName());
            log.debug("Number of activities: {} ", activityList.size());

            Double declaredDays = sumDaysByCategory(activityList, DECLARED_DAYS_CATEGORIES);
            Double billedDays = sumDaysByCategory(activityList, BILLED_DAYS_CATEGORIES);
            Double rttRedemption = sumDaysByCategory(activityList, RTT_REDEMPTION_DAYS_CATEGORIES);
            Double absenceDays = sumDaysByCategory(activityList, ABSENCE_DAYS_CATEGORIES);
            Double extraHoursInDays = sumDaysByCategory(activityList, EXTRA_HOURS_IN_DAYS_CATEGORIES);
            Double onCallHoursInDays = sumDaysByCategory(activityList, ON_CALL_HOURS_IN_DAYS_CATEGORIES);

            CompteRenduActiviteOutDto dto = CompteRenduActiviteOutDto.builder()
                    .collaboratorLastName(collaborator.getLastName())
                    .collaboratorFirstName(collaborator.getFirstName())
                    .declaredDays(declaredDays)
                    .billedDays(billedDays)
                    .rttRedemption(rttRedemption)
                    .absenceDays(absenceDays)
                    .extraHoursInDays(extraHoursInDays)
                    .onCallHoursInDays(onCallHoursInDays)
                    .build();

            log.debug("Processing finished for Collaborator: {}", collaborator.getFirstName() + collaborator.getLastName());
            log.debug("CRA item to append : {}",dto);

            cra.add(dto);
        });

        return cra;

    }
}
