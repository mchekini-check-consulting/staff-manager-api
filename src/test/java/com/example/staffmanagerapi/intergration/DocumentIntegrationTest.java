package com.example.staffmanagerapi.intergration;

import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.dto.document.DocumentSearchRequestDTO;
import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.repository.DocumentRepository;
import com.example.staffmanagerapi.utils.AccessTokenProvider;
import com.google.common.collect.Lists;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.staffmanagerapi.enums.DocumentTypeEnum.CARTE_VITALE;
import static com.example.staffmanagerapi.enums.DocumentTypeEnum.TRANSPORT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DocumentIntegrationTest {
    @LocalServerPort
    Integer port;

    private final TestRestTemplate restTemplate;
    private String ADMIN_ACCESS_TOKEN;
    private final CollaboratorRepository collaboratorRepository;
    private final DocumentRepository documentRepository;
    private final Flyway flyway;

    @Autowired
    public DocumentIntegrationTest(TestRestTemplate restTemplate, CollaboratorRepository collaboratorRepository, DocumentRepository documentRepository, Flyway flyway) {
        this.restTemplate = restTemplate;
        this.collaboratorRepository = collaboratorRepository;
        this.documentRepository = documentRepository;
        this.flyway = flyway;
    }


    private void initData(){

        collaboratorRepository.deleteAll();
        Collaborator collaborator1 = Collaborator.builder().id(1L)
                .lastName("Doe")
                .firstName("John")
                .address("New York City")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        Collaborator collaborator2 = Collaborator.builder().lastName("Smith").id(2L)
                .firstName("Jane")
                .address("Los Angeles")
                .email("jane.smith@example.com")
                .phone("9876543210")
                .build();

        Collaborator collaborator3 = Collaborator.builder().lastName("Johnson").id(3L)
                .firstName("Michael")
                .address("Chicago")
                .email("michael.johnson@example.com")
                .phone("5555555555")
                .build();

        collaboratorRepository.saveAll(Lists.newArrayList(collaborator1, collaborator2, collaborator3));

        documentRepository.deleteAll();
        Document document1 = Document.builder()
                .name("Document 1").type(TRANSPORT).collaborator(collaborator1).createdAt("2023-07-23")
                .build();
        Document document2 = Document.builder()
                .name("Document 2").type(CARTE_VITALE).collaborator(collaborator1).createdAt("2023-07-23")
                .build();
        Document document3 = Document.builder()
                .name("Document 3").type(TRANSPORT).collaborator(collaborator2).createdAt("2023-07-23")
                .build();
        Document document4 = Document.builder()
                .name("Document 4").type(CARTE_VITALE).collaborator(collaborator2).createdAt("2023-07-23")
                .build();
        Document document5 = Document.builder()
                .name("Document 5").type(TRANSPORT).collaborator(collaborator3).createdAt("2023-07-23")
                .build();
        Document document6 = Document.builder()
                .name("Document 6").type(CARTE_VITALE).collaborator(collaborator3).createdAt("2023-07-23")
                .build();

        documentRepository.saveAll(Lists.newArrayList(document1, document2, document3, document4, document5, document6));
    }

    @Test
    void shouldUploadDocument() {

        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "pdf", "Spring Framework" .getBytes());

        CreateDocumentDto dto = CreateDocumentDto.builder()
                .type(DocumentTypeEnum.PIECE_IDENTITE)
                .file(file)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<CreateDocumentDto> requestEntity = new HttpEntity<>(dto, headers);

//         WHEN
//        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/api/v1/justificatif", requestEntity, Object.class);

        // THEN
//        assertEquals(201, response.getStatusCode().value());
    }

    @BeforeEach
    public void setUp() {
        this.ADMIN_ACCESS_TOKEN = AccessTokenProvider.getAdminAccessToken("test-integration", "test-integration");
    }

    @BeforeAll
    public void clearDatabaseAndInitData(){
        flyway.clean();
        flyway.migrate();
        initData();
    }

    // Test case 0-0
    @Test
    void itShouldReturnAllDocumentsWithNoFilters() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        List<DocumentTypeEnum> types = new ArrayList<>();
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        // List of dummy documents inserted via the testDocument.sql
        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document3 = DocumentSearchResponseDTO.builder()
                .id(3)
                .name("Document 3")
                .collaborator("Jane Smith")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document5 = DocumentSearchResponseDTO.builder()
                .id(5)
                .name("Document 5")
                .collaborator("Michael Johnson")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document6 = DocumentSearchResponseDTO.builder()
                .id(6)
                .name("Document 6")
                .collaborator("Michael Johnson")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1, document2, document3, document4, document5, document6));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
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
    void itShouldReturnAllDocumentOfCollaboratorId1() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        List<DocumentTypeEnum> types = new ArrayList<>();
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1, document2));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
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
    void itShouldReturnDocumentsOfCollaboratorId1and2() throws Exception {
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
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document3 = DocumentSearchResponseDTO.builder()
                .id(3)
                .name("Document 3")
                .collaborator("Jane Smith")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1, document2, document3, document4));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
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
    void itShouldReturnDocumentsOfCollaboratorId1andTypeTransport() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        List<DocumentTypeEnum> types = new ArrayList<>();
        types.add(TRANSPORT);
        DocumentSearchRequestDTO request = DocumentSearchRequestDTO.builder().collaborators(collaborators).types(types).build();

        DocumentSearchResponseDTO document1 = DocumentSearchResponseDTO.builder()
                .id(1)
                .name("Document 1")
                .collaborator("John Doe")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document1));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
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
    void itShouldReturnCartesVitalesOfCollaborator1and2() throws Exception {
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
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(Arrays.asList(document2, document4));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
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
    void itShouldReturnAllDocumentsForAllFilters() throws Exception {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        collaborators.add(2L);
        collaborators.add(3L);

        List<DocumentTypeEnum> types = new ArrayList<>();
        types.add(DocumentTypeEnum.CARTE_VITALE);
        types.add(TRANSPORT);
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
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document2 = DocumentSearchResponseDTO.builder()
                .id(2)
                .name("Document 2")
                .collaborator("John Doe")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document3 = DocumentSearchResponseDTO.builder()
                .id(3)
                .name("Document 3")
                .collaborator("Jane Smith")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document4 = DocumentSearchResponseDTO.builder()
                .id(4)
                .name("Document 4")
                .collaborator("Jane Smith")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document5 = DocumentSearchResponseDTO.builder()
                .id(5)
                .name("Document 5")
                .collaborator("Michael Johnson")
                .type(TRANSPORT)
                .createdAt("2023-07-23")
                .build();

        DocumentSearchResponseDTO document6 = DocumentSearchResponseDTO.builder()
                .id(6)
                .name("Document 6")
                .collaborator("Michael Johnson")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .createdAt("2023-07-23")
                .build();

        List<DocumentSearchResponseDTO> responseEntity = new ArrayList<>(
                Arrays.asList(document1, document2, document3, document4, document5, document6)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ADMIN_ACCESS_TOKEN);
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
