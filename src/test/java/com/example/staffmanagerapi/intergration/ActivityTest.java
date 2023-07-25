package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.dto.activity.out.CompteRenduActiviteOutDto;
import com.example.staffmanagerapi.enums.ActivityCategoryEnum;
import com.example.staffmanagerapi.model.Activity;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.ActivityRepository;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.utils.AccessTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = {"server.port=" + ActivityTest.PORT})
@ActiveProfiles("test")
@Slf4j
class ActivityTest {

    static final int PORT = 55555;

    private final TestRestTemplate restTemplate;
    private final ActivityRepository activityRepository;
    private final CollaboratorRepository collaboratorRepository;
    private String ENDPOINT;
    private String ADMIN_ACCESS_TOKEN;
    private String COLLABORATOR_ACCESS_TOKEN;

    @Autowired
    ActivityTest(TestRestTemplate restTemplate, ActivityRepository activityRepository, CollaboratorRepository collaboratorRepository) {
        this.restTemplate = restTemplate;
        this.activityRepository = activityRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    public static List<Activity> getMockActivitiesBilledDays(Collaborator collaborator) {
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
                .category(ActivityCategoryEnum.RACHAT_RTT)
                .comment("RTT Redemption")
                .build();
        activities.add(activity2);

        // Activity 3
        Activity activity3 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 1))
                .quantity(8)
                .category(ActivityCategoryEnum.INTERCONTRAT)
                .comment("")
                .build();
        activities.add(activity3);

        // Activity 4
        Activity activity4 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 21))
                .quantity(8)
                .category(ActivityCategoryEnum.RACHAT_RTT)
                .comment("RTT Redemption")
                .build();
        activities.add(activity4);
        return activities;
    }

    @BeforeEach
    public void setUp() {
        this.ENDPOINT = "http://localhost:" + PORT + "/api/v1/activity";
        this.ADMIN_ACCESS_TOKEN = AccessTokenProvider.getAdminAccessToken("test-integration", "test-integration");
        this.COLLABORATOR_ACCESS_TOKEN = AccessTokenProvider.getCollaboratorAccessToken("test-integration", "test-integration");
        this.activityRepository.deleteAll(); // vider la base a chaque requette car je ne veut pas que les calculs d'un test collide avec l'autre
        this.collaboratorRepository.deleteAll();
    }

    @Test
    void itShouldReturn200() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
        ParameterizedTypeReference<List<CompteRenduActiviteOutDto>> responseType = new ParameterizedTypeReference<>() {
        };
        // WHEN
        ResponseEntity<List<CompteRenduActiviteOutDto>> response = restTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), responseType);

        //Then
        assertEquals(200, response.getStatusCode().value());
        log.info("body : {} ", response.getBody());
    }

    @Test
    void itShouldReturn401() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        // sending without a token
        // WHEN
        ResponseEntity<Object> response = restTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), Object.class);
        //Then
        assertEquals(401, response.getStatusCode().value());
    }

    @Test
    void itShouldReturn403() throws Exception {
        // GIVEN
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(COLLABORATOR_ACCESS_TOKEN);
        // WHEN
        ResponseEntity<Object> response = restTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), Object.class);
        //Then
        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    void itShouldCalculateDeclaredDaysCorrectly_With200Status() throws Exception {
        //Given
        Collaborator collaborator = Collaborator.builder()
                .email("test-integration@check-consulting.com")
                .address("test-integration")
                .lastName("test-integration")
                .firstName("test-integration")
                .phone("0622334455")
                .build();
        Collaborator savedCollaborator = collaboratorRepository.save(collaborator);
        List<Activity> activities = getMockActivitiesBilledDays(collaborator);
        // Question : la je suis censé passer par le service ? ou je peut le zapper et passer par le repository ?
        activityRepository.saveAll(activities);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
        ParameterizedTypeReference<List<CompteRenduActiviteOutDto>> responseType = new ParameterizedTypeReference<>() {
        };
        // WHEN
        ResponseEntity<List<CompteRenduActiviteOutDto>> response = restTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), responseType);
        List<CompteRenduActiviteOutDto> cra = response.getBody();
        //Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(cra);
        assertEquals(4d, cra.get(0).getDeclaredDays());
        assertEquals(1d, cra.get(0).getBilledDays());
        assertEquals(2d, cra.get(0).getRttRedemption());
        assertEquals(0, cra.get(0).getAbsenceDays());
        assertEquals(0, cra.get(0).getExtraHoursInDays());
        assertEquals(0, cra.get(0).getOnCallHoursInDays());

    }

    @Test
    void itShouldCalculateAbsenceDaysCorrectly_With200Status() throws Exception {
        //Given
        Collaborator collaborator = Collaborator.builder()
                .email("test-integration@check-consulting.com")
                .address("test-integration")
                .lastName("test-integration")
                .firstName("test-integration")
                .phone("0622334455")
                .build();
        Collaborator savedCollaborator = collaboratorRepository.save(collaborator);
        List<Activity> activities = getMockActivitiesAbsenceDays(collaborator);
        // Question : la je suis censé passer par le service ? ou je peut le zapper et passer par le repository ?
        activityRepository.saveAll(activities);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
        ParameterizedTypeReference<List<CompteRenduActiviteOutDto>> responseType = new ParameterizedTypeReference<>() {
        };
        // WHEN
        ResponseEntity<List<CompteRenduActiviteOutDto>> response = restTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), responseType);
        List<CompteRenduActiviteOutDto> cra = response.getBody();
        log.info("{}", cra);
        //Then
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(cra);
        assertEquals(0, cra.get(0).getDeclaredDays());
        assertEquals(0, cra.get(0).getBilledDays());
        assertEquals(0, cra.get(0).getRttRedemption());
        assertEquals(4d, cra.get(0).getAbsenceDays());
        assertEquals(1, cra.get(0).getExtraHoursInDays());
        assertEquals(0, cra.get(0).getOnCallHoursInDays());

    }

    private List<Activity> getMockActivitiesAbsenceDays(Collaborator collaborator) {
        List<Activity> activities = new ArrayList<>();

        // Activity 1
        Activity activity1 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 15))
                .quantity(8)
                .category(ActivityCategoryEnum.CONGE_MATERNITE)
                .comment("CONGE_MATERNITE")
                .build();
        activities.add(activity1);

        // Activity 2
        Activity activity2 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 20))
                .quantity(8)
                .category(ActivityCategoryEnum.CONGE_PAYE)
                .comment("CONGE_PAYE")
                .build();
        activities.add(activity2);

        // Activity 3
        Activity activity3 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 23))
                .quantity(8)
                .category(ActivityCategoryEnum.CONGE_PAYE)
                .comment("CONGE_PAYE")
                .build();
        activities.add(activity3);

        // Activity 4
        Activity activity4 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 4))
                .quantity(8)
                .category(ActivityCategoryEnum.RTT)
                .comment("RTT")
                .build();
        activities.add(activity4);

        // Activity 5
        Activity activity5 = Activity.builder()
                .collaborator(collaborator)
                .date(LocalDate.of(2023, 7, 25))
                .quantity(8)
                .category(ActivityCategoryEnum.HEURE_SUPPLEMENTAIRE)
                .comment("HEURE_SUPPLEMENTAIRE")
                .build();
        activities.add(activity5);
        return activities;
    }
}
