package com.example.staffmanagerapi.collaborator;


import com.example.staffmanagerapi.model.Collaborator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CollaboratorIntegrationTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    ResponseEntity<String> lastResponse;

    @LocalServerPort
    private int port;

    @Test
    void shouldCreateCollaborator() throws Exception{

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
                "http://localhost:"+port+"/api/v1/collaborator",
                HttpMethod.POST,
                requestEntyt,
                String.class
        );

        //Then
        assertEquals(HttpStatusCode.valueOf(201),lastResponse.getStatusCode());
    }

    @Test
    void shouldGetAllCollaborators() {
        // Given
        HttpEntity<Void> requestEntity = new HttpEntity<>(null);

        // When
        ResponseEntity<Collaborator[]> responseEntity = restTemplate.exchange(
                "http://localhost:"+port+"/api/v1/collaborator",
                HttpMethod.GET,
                requestEntity,
                Collaborator[].class
        );

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


        List<Collaborator> collaborators = Arrays.asList(responseEntity.getBody());

    }



}
