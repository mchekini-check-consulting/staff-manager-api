package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.dto.mission.in.CreateMissionInDto;
import com.example.staffmanagerapi.dto.mission.in.MissionDto;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.model.Mission;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.CustomerRepository;
import com.example.staffmanagerapi.repository.MissionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MissionTestIT {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate;
    private final CollaboratorRepository collaboratorRepository;
    private final CustomerRepository customerRepository;
    private final MissionRepository missionRepository;



    @Autowired
    MissionTestIT(TestRestTemplate restTemplate, CollaboratorRepository collaboratorRepository, CustomerRepository customerRepository, MissionRepository missionRepository) {
        this.restTemplate = restTemplate;
        this.collaboratorRepository = collaboratorRepository;
        this.customerRepository = customerRepository;
        this.missionRepository = missionRepository;
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

    @Test
    void shouldGetMissionsWithSuccess() throws Exception {
        //Given

        Collaborator collaborator = Collaborator.builder()
                .email("collab1@gmail.com")
                .address("adresse test")
                .lastName("last name")
                .firstName("first name")
                .phone("0666666666")
                .build();

        collaboratorRepository.save(collaborator);

        Customer customer = Customer.builder()
                .customerEmail("sephora@gmail.com")
                .customerName("Sephora")
                .customerAddress("france")
                .customerPhone("0123456789")
                .customerTvaNumber("FR01234567890")
                .build();

        customerRepository.save(customer);


       Mission mission1 = Mission.builder()
                .nameMission("mission impossible 1")
                .startingDateMission(LocalDate.parse("2023-10-01"))
                .endingDateMission(LocalDate.parse("2023-10-30"))
                .collaborator(collaborator)
                .customer(customer)
                .customerContactFirstname("amar")
                .customerContactLastname("amar")
                .customerContactEmail("a@aa.com")
                .customerContactPhone("0789456125")
                .build();
        Mission mission2 = Mission.builder()
                .nameMission("mission impossible 2")
                .startingDateMission(LocalDate.parse("2023-10-01"))
                .endingDateMission(LocalDate.parse("2023-11-01"))
                .collaborator(collaborator)
                .customer(customer)
                .customerContactFirstname("brad")
                .customerContactLastname("pit")
                .customerContactEmail("a@aa.com")
                .customerContactPhone("0789456125")
                .build();
        missionRepository.save(mission1);
        missionRepository.save(mission2);

        //When
        ResponseEntity<List> response = new RestTemplate().exchange(
                "http://localhost:"+port+"/api/v1/mission",
                HttpMethod.GET,
                null,
                List.class
        );
        //Then
        assertEquals(HttpStatusCode.valueOf(200),response.getStatusCode());
        assertEquals(2,response.getBody().size());

    }
}
