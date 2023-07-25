package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.ActivityRepository;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.utils.AccessTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ActivityTest {

    @LocalServerPort
    private int port;
    private final TestRestTemplate restTemplate;
    private final ActivityRepository activityRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final String ENDPOINT = "/api/v1/activity";
    private String ACESS_TOKEN ;

    @BeforeEach
    public void setUp() {
        this.ACESS_TOKEN = AccessTokenProvider.getAdminAccessToken("test-integration","test-integration");
    }

    @Autowired
    ActivityTest(TestRestTemplate restTemplate, ActivityRepository activityRepository, CollaboratorRepository collaboratorRepository) {
        this.restTemplate = restTemplate;
        this.activityRepository = activityRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    @Test
    void itShouldCalculateCraCorrectly() throws Exception {

        //Given

        Collaborator collaborator = Collaborator.builder()
                .email("test-integration@check-consulting.com")
                .address("test-integration")
                .lastName("test-integration")
                .firstName("test-integration")
                .phone("0622334455")
                .build();
        Collaborator savedCollaborator = collaboratorRepository.save(collaborator);
        List<Activity> activities = getMockActivities(collaborator);
        // Question : la je suis censé passer par le service ? ou je peut le zapper et passer par le repository ?
        activityRepository.saveAll(activities);

        // WHEN

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateMissionInDto> requestEntity = new HttpEntity<>(mission, headers);

        // WHEN
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + ENDPOINT, requestEntity, Object.class);

        //Then
        assertEquals(201, response.getStatusCode().value());
    }

    public static List<Activity> getMockActivities(Collaborator collaborator) {
        List<Activity> activities = new ArrayList<>();

        // Activity 1
        Activity activity1 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 15))
                .quantity(8)
                .category(ActivityCategoryEnum.JOUR_TRAVAILLE)
                .comment("Regular workday")
                .build();
        activities.add(activity1);

        // Activity 2
        Activity activity2 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 20))
                .quantity(8)
                .category(ActivityCategoryEnum.RTT)
                .comment("RTT taken")
                .build();
        activities.add(activity2);

        // Activity 3
        Activity activity3 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 1))
                .quantity(8)
                .category(ActivityCategoryEnum.CONGE_PAYE)
                .comment("Paid leave")
                .build();
        activities.add(activity3);

        return activities;
    }
}
