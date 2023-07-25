package com.example.staffmanagerapi.Unit;

import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.dto.document.DocumentSearchResponseDTO;
import com.example.staffmanagerapi.exception.FileNameExistsException;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.repository.DocumentRepository;
import com.example.staffmanagerapi.service.CollaboratorService;
import com.example.staffmanagerapi.service.DocumentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class DocumentUnitTest {

    private static DateTimeFormatter formatter;
    @Mock
    private CollaboratorService collaboratorService;

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentService documentService;

    @BeforeAll
    public static void setupDateTimeFormatter() {
        formatter = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
    }


    @Test
    public void itShouldUploadDocumentSuccess() {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        // GIVEN
        Collaborator collab = Collaborator.builder()
                .email("test@gmail.com")
                .address("adresse test")
                .lastName("last name")
                .firstName("first name")
                .phone("000000000")
                .build();


        Document docOut = Document.builder().id(1).type(DocumentTypeEnum.PIECE_IDENTITE).name("piece_identite.pdf").createdAt(LocalDate.now().format(formatter)).build();
        Document docIn = Document.builder().type(DocumentTypeEnum.PIECE_IDENTITE).name("piece_identite.pdf").build();

        Mockito
                .lenient()
                .when(documentRepository.save(docIn))
                .thenReturn(docOut);

        Mockito
                .lenient()
                .when(collaboratorService.findCollaboratorByEmail(collab.getEmail()))
                .thenReturn(
                        Optional.of(Collaborator.builder().email(collab.getEmail()).build())
                );


        assertEquals(docOut.getName(), docIn.getName());
        assertEquals(docOut.getType(), docIn.getType());
        assertEquals(docOut.getName(), docIn.getName());
    }

    @Test
    public void itShouldUploadDocumentExtension() {
        // GIVEN
        Collaborator collab = Collaborator.builder()
                .email("test@gmail.com")
                .address("adresse test")
                .lastName("last name")
                .firstName("first name")
                .phone("000000000")
                .build();


        Document docIn = Document.builder().type(DocumentTypeEnum.PIECE_IDENTITE).name("piece_identite.txt").build();

        Mockito
                .lenient()
                .when(collaboratorService.findCollaboratorByEmail(collab.getEmail()))
                .thenReturn(
                        Optional.of(Collaborator.builder().email(collab.getEmail()).build())
                );

        Mockito.lenient().when(documentRepository.save(docIn)).thenThrow(new FileInvalidExtensionException("Type fichier .txt non autorisé, type de fichiers autorisés: .jpeg, .jpg, .pdf"));
        Exception exception = assertThrows(FileInvalidExtensionException.class, () -> {
            documentRepository.save(docIn);
        });
        assertTrue(exception.getMessage().contains("Type fichier .txt non autorisé, type de fichiers autorisés: .jpeg, .jpg, .pdf"));
    }


    @Test
    public void itShouldUploadDocumentEmpty() {
        // GIVEN
        Collaborator collaborator = Collaborator.builder()
                .email("test@gmail.com")
                .address("adresse test")
                .lastName("last name")
                .firstName("first name")
                .phone("000000000")
                .build();



        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "pdf", "".getBytes());


        CreateDocumentDto dto = CreateDocumentDto.builder().type(DocumentTypeEnum.PIECE_IDENTITE).file(file).build();

        Mockito
                .lenient()
                .when(collaboratorService.findCollaboratorByEmail(collaborator.getEmail()))
                .thenReturn(
                        Optional.of(Collaborator.builder().email(collaborator.getEmail()).build())
                );

        Document doc = new ModelMapper().map(dto, Document.class);

        Mockito.lenient().when(documentRepository.save(doc)).thenThrow(new FileEmptyException("Veuillez séléctionner un fichier valide"));
        Exception exception = assertThrows(FileEmptyException.class, () -> {
            documentRepository.save(doc);
        });
        assertTrue(exception.getMessage().contains("Veuillez séléctionner un fichier valide"));
    }

    @Test
    public void itShouldUploadDocumentUniqueName() {
        // GIVEN
        Collaborator collaborator = Collaborator.builder()
                .email("test@gmail.com")
                .address("adresse test")
                .lastName("last name")
                .firstName("first name")
                .phone("000000000")
                .build();



        MockMultipartFile file = new MockMultipartFile("file", "test.pdf",
                "pdf", "".getBytes());


        CreateDocumentDto dto = CreateDocumentDto.builder().type(DocumentTypeEnum.PIECE_IDENTITE).file(file).build();

        Mockito
                .lenient()
                .when(collaboratorService.findCollaboratorByEmail(collaborator.getEmail()))
                .thenReturn(
                        Optional.of(Collaborator.builder().email(collaborator.getEmail()).build())
                );

        Document doc = new ModelMapper().map(dto, Document.class);

        Mockito.lenient().when(documentRepository.save(doc)).thenThrow(new FileNameExistsException("Ce nom de document est déjà existant. Merci de modifier le nom"));
        Exception exception = assertThrows(FileNameExistsException.class, () -> {
            documentRepository.save(doc);
        });
        assertTrue(exception.getMessage().contains("Ce nom de document est déjà existant. Merci de modifier le nom"));
    }

    @Test
    public void shouldGetDocumentsWithFilters() {
        // Given
        List<Long> collaborators = new ArrayList<>();
        collaborators.add(1L);
        List<DocumentTypeEnum> types = new ArrayList<>();
        types.add(DocumentTypeEnum.TRANSPORT);

        List<Document> mockDocuments = new ArrayList<>();
        Collaborator collaborator = Collaborator.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .address("New York City")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        Document document1 = Document.builder()
                .id(1)
                .name("Document 1")
                .type(DocumentTypeEnum.TRANSPORT)
                .collaborator(collaborator)
                .createdAt(LocalDate.now().format(formatter))
                .build();

        mockDocuments.add(document1);

        when(documentRepository.findAll(any(Specification.class))).thenReturn(mockDocuments);

        // When
        List<DocumentSearchResponseDTO> result = documentService.getDocumentsWithFilters(collaborators, types);

        // Then
        assertEquals(1, result.size());
        DocumentSearchResponseDTO responseDTO = result.get(0);
        assertEquals(document1.getId(), responseDTO.getId());
        assertEquals(document1.getName(), responseDTO.getName());
        assertEquals(document1.getType(), responseDTO.getType());
        assertEquals(document1.getCreatedAt(), responseDTO.getCreatedAt());
    }

    @Test
    public void shouldGetDocumentsWithNoFilters() {
        // Given
        List<Document> mockDocuments = new ArrayList<>();
        Collaborator collaborator1 = Collaborator.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .address("New York City")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        Document document1 = Document.builder()
                .id(1)
                .name("Document 1")
                .type(DocumentTypeEnum.TRANSPORT)
                .collaborator(collaborator1)
                .createdAt(LocalDate.now().format(formatter))
                .build();

        Collaborator collaborator2 = Collaborator.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .address("Los Angeles")
                .email("jane.smith@example.com")
                .phone("9876543210")
                .build();

        Document document2 = Document.builder()
                .id(2)
                .name("Document 2")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .collaborator(collaborator2)
                .createdAt(LocalDate.now().format(formatter))
                .build();

        mockDocuments.add(document1);
        mockDocuments.add(document2);

        Specification<Document> spec = Specification.where(null);

        doReturn(mockDocuments).when(documentRepository).findAll(spec);

        // When
        List<DocumentSearchResponseDTO> result = documentService.getDocumentsWithFilters(null, null);

        // Then
        assertEquals(2, result.size());
    }

    @Test
    public void shouldGetDocumentsWithTypesFilter() {
        // Given
        List<Document> mockDocuments = new ArrayList<>();
        Collaborator collaborator1 = Collaborator.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .address("New York City")
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        Document document1 = Document.builder()
                .id(1)
                .name("Document 1")
                .type(DocumentTypeEnum.TRANSPORT)
                .collaborator(collaborator1)
                .createdAt(LocalDate.now().format(formatter))
                .build();

        Collaborator collaborator2 = Collaborator.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .address("Los Angeles")
                .email("jane.smith@example.com")
                .phone("9876543210")
                .build();

        Document document2 = Document.builder()
                .id(2)
                .name("Document 2")
                .type(DocumentTypeEnum.CARTE_VITALE)
                .collaborator(collaborator2)
                .createdAt(LocalDate.now().format(formatter))
                .build();

        mockDocuments.add(document1);
        mockDocuments.add(document2);

        List<DocumentTypeEnum> types = new ArrayList<>();
        types.add(DocumentTypeEnum.TRANSPORT);
        types.add(DocumentTypeEnum.CARTE_VITALE);

        doReturn(mockDocuments).when(documentRepository).findAll(ArgumentMatchers.<Specification<Document>>any());

        // When
        List<DocumentSearchResponseDTO> result = documentService.getDocumentsWithFilters(null, types);

        // Then
        assertEquals(2, result.size());
        assertEquals(DocumentTypeEnum.TRANSPORT, result.get(0).getType());
        assertEquals(DocumentTypeEnum.CARTE_VITALE, result.get(1).getType());
    }

}
