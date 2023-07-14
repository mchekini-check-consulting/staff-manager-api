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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerTest {

    @LocalServerPort
    Integer port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void itShouldCreateCustomer() {
        Customer customer = Customer.builder()
                .customerEmail("natixis2@gmail.com")
                .customerName("NATIXIS")
                .customerAddress("Paris")
                .customerPhone("004199111")
                .customerTvaNumber("fr0123456789")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/customer",
                requestEntity,
                Object.class
        );

        Map<?, ?> responseHandler = convertResponseEntityToMap(response.getBody());
        Map<String, String> resultMap = convertFieldsErrorsToHashMap(responseHandler.get("data"));

        //Errors
        assertEquals(400, responseHandler.get("statusCode"));
        //--Email exist
//        assertEquals("Email existe deja", responseHandler.get("message"));
        //--customerPhone validation
        assertEquals(true, resultMap.keySet().contains("customerPhone"));
        //--customerTvaNumber validation
        assertEquals(true, resultMap.keySet().contains("customerTvaNumber"));

//        success
//        assertEquals(201, responseHandler.get("statusCode"));

    }

    public Map convertResponseEntityToMap(Object liste) {
        Map<?, ?> data = null;
        if (liste instanceof Map) {
            data = (Map<?, ?>) liste;
            return data;
        }
        return null;
    }

    public Map<String, String> convertFieldsErrorsToHashMap(Object liste) {
        if (liste instanceof List) {
            List<?> dataList = (List<?>) liste;
            Map<String, String> resultMap = new HashMap<>();

            for (Object item : dataList) {
                if (item instanceof Map) {
                    Map<?, ?> itemData = (Map<?, ?>) item;
                    String field = (String) itemData.get("field");
                    String errorMessage = (String) itemData.get("errorMessage");
                    resultMap.put(field, errorMessage);
                }
            }
            return resultMap;
        }
        return null;
    }
}