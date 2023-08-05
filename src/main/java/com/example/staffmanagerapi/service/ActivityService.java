package com.example.staffmanagerapi.service;

import static com.example.staffmanagerapi.utils.Constants.*;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import com.example.staffmanagerapi.dto.activity.out.CompteRenduActiviteOutDto;
import com.example.staffmanagerapi.dto.mission.in.MissionDto;
import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Mission;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.ActivityRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityService {

  private final ActivityRepository activityRepository;
  private final CollaboratorService collaboratorService;
  private final MissionService missionService;

  public ActivityService(
    ActivityRepository activityRepository1,
    CollaboratorService collaboratorService1,
    MissionService missionService1
  ) {
    this.activityRepository = activityRepository1;
    this.collaboratorService = collaboratorService1;
    this.missionService = missionService1;
  }

  public static Double sumDaysByCategory(
    List<Activity> activities,
    Set<ActivityCategoryEnum> categories
  ) {
    return hoursToDays(
      activities
        .stream()
        .filter(activity ->
          categories.stream().anyMatch(cat -> activity.getCategory() == cat)
        )
        .mapToInt(Activity::getQuantity)
        .sum()
    );
  }

  public static Double hoursToDays(Integer hours) {
    // Create a BigDecimal object from the input value and round the BigDecimal value to three decimal places.
    BigDecimal decimalValue = new BigDecimal(hours)
      .divide(BigDecimal.valueOf(HOURS_TO_DAY_RATIO), 3, RoundingMode.HALF_UP);

    Double finalResult = decimalValue.doubleValue();

    log.debug("conveting {} hours to {} days ", hours, finalResult);
    return finalResult;
  }

  public List<Activity> createActivities(User user, List<ActivityDto> data) {
    Optional<Collaborator> collaborator =
      this.collaboratorService.findCollaboratorByEmail(user.getEmail());

    if (!collaborator.isPresent()) throw new EntityNotFoundException(
      "Collaborator doesn't exist."
    );

    List<Mission> missions =
      this.missionService.getCollaboratorMissions(collaborator.get());

    List<Activity> records = data
      .stream()
      .map(row -> {
        List<Mission> activeMissions = missions
          .stream()
          .filter(missionRow -> {
            return (
              row.getDate().isAfter(missionRow.getStartingDateMission()) &&
              row.getDate().isBefore(missionRow.getEndingDateMission())
            );
          })
          .toList();

        return Activity
          .builder()
          .date(row.getDate())
          .quantity(row.getQuantity())
          .category(row.getCategory())
          .comment(row.getComment())
          .collaborator(collaborator.get())
          .mission(activeMissions.size() > 0 ? activeMissions.get(0) : null)
          .build();
      })
      .toList();

    return this.activityRepository.saveAll(records);
  }

  /**
   * <h1>Etapes a suivre:</h1>
   * <ol>
   *     <li>fetchAll activités de la base</li>
   *     <li>filtrer la liste pour le mois courant</li>
   *     <li>creer une <code>Map</code> de Collaborateur et sa liste d'activités </li>
   *     <li>parcourir la <code>Map</code>, et pour chaque collaborateur faire le calcul selon les régles définies dans l'US-141</li>
   *     <li>construire un DTO et le retourner comme réponse</li>
   * </ol>
   *
   * @return le compte rendu d'activité qui est une <code>List of CompteRenduActiviteOutDto </code>
   * @author Abdallah
   * @see #hoursToDays(Integer)
   * @see #sumDaysByCategory(List, Set)
   * @since 7/23/2023
   */
  public List<CompteRenduActiviteOutDto> getCurrentMonthCRA() {
    // constants
    YearMonth currentMonth = YearMonth.now();
    log.info(
      "Attempting to fetch compte-rendu-activité for current month : {} ...",
      currentMonth
    );
    // traitement activitiés
    Map<Collaborator, List<Activity>> activities =
      this.activityRepository.findAll()
        .stream()
        .filter(activity -> activity.getCollaborator() != null) // remove activities with no collaborator
        .filter(activity ->
          YearMonth.from(activity.getDate()).equals(currentMonth)
        ) // filter to current month only
        .collect(Collectors.groupingBy(Activity::getCollaborator)); // turn List into a Map

    // une fois groupé par collaborateur dans un Map<Collaborator,List<Activity>>
    // on map sur un DTO List<CompteRenduActiviteOutDto>

    List<CompteRenduActiviteOutDto> cra = new ArrayList<>();

    activities.forEach((collaborator, activityList) -> {
      log.debug(
        "Processing Collaborator: {}",
        collaborator.getFirstName() + collaborator.getLastName()
      );
      log.debug("Number of activities: {} ", activityList.size());

      Double declaredDays = sumDaysByCategory(
        activityList,
        DECLARED_DAYS_CATEGORIES
      );
      Double billedDays = sumDaysByCategory(
        activityList,
        BILLED_DAYS_CATEGORIES
      );
      Double rttRedemption = sumDaysByCategory(
        activityList,
        RTT_REDEMPTION_DAYS_CATEGORIES
      );
      Double absenceDays = sumDaysByCategory(
        activityList,
        ABSENCE_DAYS_CATEGORIES
      );
      Double extraHoursInDays = sumDaysByCategory(
        activityList,
        EXTRA_HOURS_IN_DAYS_CATEGORIES
      );
      Double onCallHoursInDays = sumDaysByCategory(
        activityList,
        ON_CALL_HOURS_IN_DAYS_CATEGORIES
      );

      CompteRenduActiviteOutDto dto = CompteRenduActiviteOutDto
        .builder()
        .collaboratorLastName(collaborator.getLastName())
        .collaboratorFirstName(collaborator.getFirstName())
        .declaredDays(declaredDays)
        .billedDays(billedDays)
        .rttRedemption(rttRedemption)
        .absenceDays(absenceDays)
        .extraHoursInDays(extraHoursInDays)
        .onCallHoursInDays(onCallHoursInDays)
        .build();

      log.debug(
        "Processing finished for Collaborator: {}",
        collaborator.getFirstName() + collaborator.getLastName()
      );
      log.debug("CRA item to append : {}", dto);

      cra.add(dto);
    });
    return cra;
  }

  Boolean shouldAddMission(ActivityCategoryEnum category) {
    if (category == ActivityCategoryEnum.JOUR_TRAVAILLE) return true;
    if (category == ActivityCategoryEnum.HEURE_SUPPLEMENTAIRE) return true;
    if (category == ActivityCategoryEnum.ASTREINTE) return true;

    return false;
  }
}
