package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.model.Customer;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomerTest {

    @LocalServerPort
    Integer port;

    private final TestRestTemplate restTemplate;

    @Autowired
    public CustomerTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void ItShouldGetCustomers() {
        ResponseEntity<Object> response = restTemplate.getForEntity("http://localhost:" + port + "/api/v1/customer", Object.class);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void ItShouldAddNewCustomerWithSuccess() {

        // GIVEN
        Customer customer = Customer.builder()
                .customerEmail("kpmg@gmail.com")
                .customerName("kpmg")
                .customerAddress("france")
                .customerPhone("0123456719")
                .customerTvaNumber("FR01234597890")
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        // WHEN
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/customer", requestEntity, Object.class);

        // THEN
        assertEquals(201, response.getStatusCode().value());
    }

    @Test
    void ItShouldDisplayAnErrorWhenPhoneNumberIsIncorrect() {

        // GIVEN
        Customer customer = Customer.builder()
                .customerEmail("sephora@gmail.com")
                .customerName("Sephora")
                .customerAddress("france")
                .customerPhone("012345")
                .customerTvaNumber("FR01234567890")
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        // WHEN
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/customer", requestEntity, Object.class);

        // THEN
        Map<String, String> errors = (Map<String, String>) response.getBody();

        assertEquals(400, response.getStatusCode().value());
        assertTrue(errors.keySet().contains("customerPhone"));
    }

    @Test
    void ItShouldDisplayAnErrorWhenTvaNumberIsIncorrect() {

        // GIVEN
        Customer customer = Customer.builder()
                .customerEmail("sephora@gmail.com")
                .customerName("Sephora")
                .customerAddress("france")
                .customerPhone("0123456789")
                .customerTvaNumber("FR012345")
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        // WHEN
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/customer", requestEntity, Object.class);

        // THEN
        Map<String, String> errors = (Map<String, String>) response.getBody();

        assertEquals(400, response.getStatusCode().value());
        assertTrue(errors.keySet().contains("customerTvaNumber"));
    }

    @Test
    void ItShouldDisplayAnErrorWhenEmailAlreadyExists() {

        // GIVEN
        Customer customer = Customer.builder()
                .customerEmail("sephora@gmail.com")
                .customerName("Sephora")
                .customerAddress("france")
                .customerPhone("0123456789")
                .customerTvaNumber("FR01234567890")
                .build();


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/customer", requestEntity, Object.class);


        // WHEN
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/customer", requestEntity, Object.class);

        // THEN
        Map<String, String> errors = (Map<String, String>) response.getBody();

        assertEquals(400, response.getStatusCode().value());
        assertTrue(errors.keySet().contains("customerEmail"));
        assertTrue(errors.get("customerEmail").equals("l'email du client existe déjà"));
    }

    @Test
    void ItShouldDisplayAnErrorWhenEmailAndPhoneAndTvaNumAndNameAlreadyExist() {

        // GIVEN
        Customer customer = Customer.builder()
                .customerEmail("sephora@gmail.com")
                .customerName("Sephora")
                .customerAddress("france")
                .customerPhone("0123456789")
                .customerTvaNumber("FR01234567890")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);
        restTemplate.postForEntity("http://localhost:" + port + "/api/v1/customer", requestEntity, Object.class);


        // WHEN
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/customer", requestEntity, Object.class);

        // THEN
        Map<String, String> errors = (Map<String, String>) response.getBody();
        log.info("errors==" + errors);
        assertEquals(400, response.getStatusCode().value());
        assertTrue(errors.keySet().contains("customerEmail"));
        assertTrue(errors.keySet().contains("customerPhone"));
        assertTrue(errors.keySet().contains("customerTvaNumber"));
        assertTrue(errors.keySet().contains("customerName"));

        assertTrue(errors.get("customerEmail").equals("l'email du client existe déjà"));
        assertTrue(errors.get("customerPhone").equals("Numéro de téléphone existe déja"));
        assertTrue(errors.get("customerTvaNumber").equals("Numéro du TVA existe déja"));
        assertTrue(errors.get("customerName").equals("Nom du client existe déja"));
    }
}