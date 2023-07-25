package com.example.staffmanagerapi.Unit;

import com.example.staffmanagerapi.dto.document.CreateDocumentDto;
import com.example.staffmanagerapi.enums.DocumentTypeEnum;
import com.example.staffmanagerapi.exception.FileEmptyException;
import com.example.staffmanagerapi.exception.FileInvalidExtensionException;
import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.model.Document;
import com.example.staffmanagerapi.repository.DocumentRepository;
import com.example.staffmanagerapi.service.CollaboratorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DocumentUnitTest {


    @Mock
    private CollaboratorService collaboratorService;

    @Mock
    private DocumentRepository documentRepository;


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
}
