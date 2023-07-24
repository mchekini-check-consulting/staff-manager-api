package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.ActivityRepository;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.utils.AccessTokenProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ActivityTestIT {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final ActivityRepository activityRepository;
    private final CollaboratorRepository collaboratorRepository;

    private final String ENDPOINT = "/api/v1/activity";

    @Autowired
    ActivityTestIT(TestRestTemplate restTemplate, ActivityRepository activityRepository, CollaboratorRepository collaboratorRepository) {
        this.restTemplate = restTemplate;
        this.activityRepository = activityRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    @Test
    @Disabled
    void shouldCreateMissionWithSuccess() throws Exception {

        //Given

        String access_token = AccessTokenProvider.getAdminAccessToken("mchekini","test");

        Collaborator collaborator = Collaborator.builder()
                .email("abdallah-collab@gmail.com")
                .address("test")
                .lastName("test last name")
                .firstName("test first name")
                .phone("0622334455")
                .build();
        Long collaboratorId = collaboratorRepository.save(collaborator).getId();

        // Question : la je suis censé passer par le service ? ou je peut le zapper et passer par le repository ?


        activityRepository.saveAll();

//        CreateActivityInDto activityInDto = CreateActivityInDto.builder()
//                .activities(
//                        ActivityDto.builder()
//                            .date()
//                            .build()
//                )
//                .build();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<CreateMissionInDto> requestEntity = new HttpEntity<>(mission, headers);
//
//        // WHEN
//        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + ENDPOINT, requestEntity, Object.class);
//
//        //Then
//        assertEquals(201, response.getStatusCode().value());
    }
}
