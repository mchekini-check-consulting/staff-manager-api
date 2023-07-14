package com.example.staffmanagerapi.Unit;

import com.example.staffmanagerapi.dto.activity.ActivityDto;
import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.repository.ActivityRepository;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.service.ActivityService;
import com.example.staffmanagerapi.service.CollaboratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
public class ActivityUnitTest {

    @InjectMocks
    private ActivityService activityService;

    @Mock
    private CollaboratorService collaboratorService;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Test
    public void itShouldCreateAnActivity() {
        // GIVEN
        User user = User.builder().email("test@test.com").build();

        Activity activity = Activity
                .builder()
                .id(1)
                .date(LocalDate.now())
                .quantity(8)
                .category(ActivityCategoryEnum.ASTREINTE)
                .comment("comment")
                .build();

        Mockito
                .lenient()
                .when(activityRepository.saveAll(anyList()))
                .thenReturn(List.of(activity));

        Mockito
                .lenient()
                .when(collaboratorService.findCollaboratorByEmail(user.getEmail()))
                .thenReturn(
                        Optional.of(Collaborator.builder().email(user.getEmail()).build())
                );

        // WHERE
        List<Activity> newActivities =
                this.activityService.createActivities(
                        user,
                        List.of(
                                ActivityDto
                                        .builder()
                                        .date(activity.getDate())
                                        .category(activity.getCategory())
                                        .quantity(activity.getQuantity())
                                        .comment(activity.getComment())
                                        .build()
                        )
                );

        // THEN
        assertEquals(activity.getDate(), newActivities.get(0).getDate());
        assertEquals(activity.getQuantity(), newActivities.get(0).getQuantity());
        assertEquals(activity.getCategory(), newActivities.get(0).getCategory());
    }
}
