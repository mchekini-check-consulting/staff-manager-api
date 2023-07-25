package com.example.staffmanagerapi.collaborator;


import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CollaboratorIntegrationTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    ResponseEntity<String> lastResponse;

    @LocalServerPort
    private int port;

    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    CollaboratorIntegrationTest(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    @Test
    void shouldCreateCollaborator() {

        //Given
        HttpEntity<Collaborator> requestEntyt = new HttpEntity<>(Collaborator.builder()
                .firstName("firstname")
                .lastName("lastname")
                .email("mail@email.com")
                .phone("0648751238")
                .address("test address")
                .build());
        //When

        lastResponse = new RestTemplate().exchange(
                "http://localhost:" + port + "/api/v1/collaborator",
                HttpMethod.POST,
                requestEntyt,
                String.class
        );

        //Then
        assertEquals(HttpStatusCode.valueOf(201), lastResponse.getStatusCode());
    }

    @Test
    void shouldGetAllCollaborators() {
        // Given
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);
        initData();

        // When
        ResponseEntity<Collaborator[]> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/collaborator",
                HttpMethod.GET,
                requestEntity,
                Collaborator[].class
        );

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(10, responseEntity.getBody().length);


    }

    private void initData() {
        List<Collaborator> collaborators = Lists.newArrayList();

        collaboratorRepository.deleteAll();
        for (int i = 0; i < 10; i++) {
            Collaborator collaborator = Collaborator.builder()
                    .email("collaborator" + i + "@gmail.com")
                    .phone("0695562547")
                    .address("16 rue des palmiers 75015 Paris")
                    .firstName("collaborator first name " + i)
                    .lastName("collaborator last name" + i)
                    .build();
            collaborators.add(collaborator);
        }
        collaboratorRepository.saveAll(collaborators);
    }


}
