package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.dto.document.DocumentSearchRequestDTO;
import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "classpath:testDocument.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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

    // Test case 0-0
    @Test
    @DirtiesContext
    public void itShouldReturnAllDocumentsWithNoFilters() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        List<DocumentTypeEnum> types = new ArrayList<>();
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        // List of dummy documents inserted via the testDocument.sql
        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document3 = DocumentSearchResponseDTO.builder()
                .id(3)
                .name("Document 3")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document5 = DocumentSearchResponseDTO.builder()
                .id(5)
                .name("Document 5")
                .collaborator("Michael Johnson")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document6 = DocumentSearchResponseDTO.builder()
                .id(6)
                .name("Document 6")
                .collaborator("Michael Johnson")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1, document2, document3, document4, document5, document6));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocumentSearchRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<List<DocumentSearchResponseDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/justificatif/search",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<DocumentSearchResponseDTO>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DocumentSearchResponseDTO> responseBody = response.getBody();
        assertEquals(responseEntity, responseBody);
    }

    // Test case 1-0
    @Test
    @DirtiesContext
    public void itShouldReturnAllDocumentOfCollaboratorId1() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        List<DocumentTypeEnum> types = new ArrayList<>();
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1, document2));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocumentSearchRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<List<DocumentSearchResponseDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/justificatif/search",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<DocumentSearchResponseDTO>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DocumentSearchResponseDTO> responseBody = response.getBody();
        assertEquals(responseEntity, responseBody);
    }

    // Test case 2-0
    @Test
    @DirtiesContext
    public void itShouldReturnDocumentsOfCollaboratorId1and2() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        collaborators.add(2L);
        List<DocumentTypeEnum> types = new ArrayList<>();
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document3 = DocumentSearchResponseDTO.builder()
                .id(3)
                .name("Document 3")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1, document2, document3, document4));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocumentSearchRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<List<DocumentSearchResponseDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/justificatif/search",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<DocumentSearchResponseDTO>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DocumentSearchResponseDTO> responseBody = response.getBody();
        assertEquals(responseEntity, responseBody);
    }

    // Test case 1-1
    @Test
    @DirtiesContext
    public void itShouldReturnDocumentsOfCollaboratorId1andTypeTransport() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        List<DocumentTypeEnum> types = new ArrayList<>();
        types.add(DocumentTypeEnum.TRANSPORT);
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocumentSearchRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<List<DocumentSearchResponseDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/justificatif/search",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<DocumentSearchResponseDTO>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DocumentSearchResponseDTO> responseBody = response.getBody();
        assertEquals(responseEntity, responseBody);
    }

    // Test case 2-1
    @Test
    @DirtiesContext
    public void itShouldReturnCartesVitalesOfCollaborator1and2() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        collaborators.add(2L);
        List<DocumentTypeEnum> types = new ArrayList<>();
        types.add(DocumentTypeEnum.CARTE_VITALE);
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document2, document4));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocumentSearchRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<List<DocumentSearchResponseDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/justificatif/search",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<DocumentSearchResponseDTO>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DocumentSearchResponseDTO> responseBody = response.getBody();
        assertEquals(responseEntity, responseBody);
    }

    // Test case n-n
    @Test
    @DirtiesContext
    public void itShouldReturnAllDocumentsForAllFilters() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        collaborators.add(2L);
        collaborators.add(3L);

        List<DocumentTypeEnum> types = new ArrayList<>();
        types.add(DocumentTypeEnum.CARTE_VITALE);
        types.add(DocumentTypeEnum.TRANSPORT);
        types.add(DocumentTypeEnum.AUTRE);
        types.add(DocumentTypeEnum.PIECE_IDENTITE);

        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder()
                .collaborators(collaborators)
                .types(types)
                .build();

        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document3 = DocumentSearchResponseDTO.builder()
                .id(3)
                .name("Document 3")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document5 = DocumentSearchResponseDTO.builder()
                .id(5)
                .name("Document 5")
                .collaborator("Michael Johnson")
                .type(DocumentTypeEnum.TRANSPORT)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        DocumentSearchResponseDTO document6 = DocumentSearchResponseDTO.builder()
                .id(6)
                .name("Document 6")
                .collaborator("Michael Johnson")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt(LocalDate.of(2023, 7, 23))
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(
                Arrays.asList(document1, document2, document3, document4, document5, document6)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DocumentSearchRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        // When
        ResponseEntity<List<DocumentSearchResponseDTO>> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/justificatif/search",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<DocumentSearchResponseDTO>>() {}
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<DocumentSearchResponseDTO> responseBody = response.getBody();
        assertEquals(responseEntity, responseBody);
    }



}
