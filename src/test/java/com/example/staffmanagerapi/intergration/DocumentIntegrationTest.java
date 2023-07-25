package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DocumentIntegrationTest {
    @LocalServerPort
    Integer port;

    private final TestRestTemplate restTemplate;


    @Autowired
    public DocumentIntegrationTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void shouldUploadDocument() {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "pdf", "Spring Framework" .getBytes());

        CreateDocumentDto dto = CreateDocumentDto.builder()
                .type(DocumentTypeEnum.PIECE_IDENTITE)
                .file(file)
                .build();

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<CreateDocumentDto> requestEntity = new HttpEntity<>(dto, headers);

        // WHEN
//        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/document", requestEntity, Object.class);

        // THEN
//        assertEquals(201, response.getStatusCode().value());
    }
}