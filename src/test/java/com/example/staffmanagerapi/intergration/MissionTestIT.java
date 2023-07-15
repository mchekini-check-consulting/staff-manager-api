package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MissionTestIT {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final CollaboratorRepository collaboratorRepository;
    private final CustomerRepository customerRepository;


    @Autowired
    MissionTestIT(TestRestTemplate restTemplate, CollaboratorRepository collaboratorRepository, CustomerRepository customerRepository) {
        this.restTemplate = restTemplate;
        this.collaboratorRepository = collaboratorRepository;
        this.customerRepository = customerRepository;
    }


    @Test
    void shouldCreateMissionWithSuccess() throws Exception {

        //Given

        Collaborator collaborator = Collaborator.builder()
                .email("collab1@gmail.com")
                .address("adresse test")
                .lastName("last name")
                .firstName("first name")
                .phone("0666666666")
                .build();

        Long collaboratorId = collaboratorRepository.save(collaborator).getId();

        Customer customer = Customer.builder()
                .customerEmail("sephora@gmail.com")
                .customerName("Sephora")
                .customerAddress("france")
                .customerPhone("0123456789")
                .customerTvaNumber("FR01234567890")
                .build();

        Long customerId = customerRepository.save(customer).getCustomerId();


        CreateMissionInDto mission = CreateMissionInDto.builder()
                .nameMission("mission 1")
                .startingDateMission("12/01/2023")
                .endingDateMission("12/01/2024")
                .collaboratorId(collaboratorId)
                .customerId(customerId)
                .customerContactFirstname("amar")
                .customerContactLastname("amar")
                .customerContactEmail("a@aa.com")
                .customerContactPhone("0789456125")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateMissionInDto> requestEntity = new HttpEntity<>(mission, headers);


        // WHEN
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/mission", requestEntity, Object.class);


        //Then
        assertEquals(201, response.getStatusCode().value());
    }
}
