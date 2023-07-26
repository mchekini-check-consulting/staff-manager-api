package com.example.staffmanagerapi.Unit;

import com.example.staffmanagerapi.dto.mission.in.MissionDto;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Customer;
import com.example.staffmanagerapi.model.Mission;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
class MissionUnitTest {
    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void WhenConvertMissionEntityToMissionDTO_thenCorrect(){

        //GIVEN
        Customer customer = Customer.builder()
                .customerEmail("sephora@gmail.com")
                .customerName("Sephora")
                .customerAddress("france")
                .customerPhone("0123456789")
                .customerTvaNumber("FR01234567890")
                .build();

        Collaborator collaborator = Collaborator.builder()
                .email("collab1@gmail.com")
                .address("adresse test")
                .lastName("collab lname")
                .firstName("collab fame")
                .phone("0666666666")
                .build();


        Mission mission = Mission.builder()
                .nameMission("mission impossible 1")
                .startingDateMission(LocalDate.parse("2023-10-01"))
                .endingDateMission(LocalDate.parse("2023-10-30"))
                .collaborator(collaborator)
                .customer(customer)
                .customerContactFirstname("customer contact fname")
                .customerContactLastname("customer contact lname")
                .customerContactEmail("cutomer@contact.com")
                .customerContactPhone("0789456125")
                .build();
        //WHEN
        MissionDto missionDto = modelMapper.map(mission, MissionDto.class);

        //THEN
        assertEquals(mission.getId(), missionDto.getId());
        assertEquals(mission.getCustomer().getCustomerName(), missionDto.getCustomerName());
        assertEquals(mission.getCustomerContactLastname(), missionDto.getCustomerContactLastname());
    }
}
