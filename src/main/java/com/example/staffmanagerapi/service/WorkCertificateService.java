package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.model.Collaborator;
import com.example.staffmanagerapi.repository.CollaboratorRepository;
import com.example.staffmanagerapi.utils.Constants;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class WorkCertificateService {

    CollaboratorRepository collaboratorRepository;

    public WorkCertificateService(CollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    public byte[] generateNewCertificate(Long id) throws Exception {

        Optional<Collaborator> optional = collaboratorRepository.findById(id);

        log.info("start generating work-certificate for collaborator : {} ", optional);

        if (optional.isEmpty()) {
            throw new EntityNotFoundException("Ce collaborateur n'existe pas");
        } else {
            Collaborator collaborator = optional.get();

            Map<String, String> workCertificateVariables = new HashMap<>();

            workCertificateVariables = updateVariables(workCertificateVariables, collaborator);

            String inputFilePath = Constants.WORK_CERTIFICATE_TEMPLATE_URL;

            byte[] updatedDoc = this.replaceText(inputFilePath, workCertificateVariables);
            return this.convertDocxToPdf(updatedDoc);
        }
    }

    public byte[] convertDocxToPdf(byte[] docxBytes) throws IOException {
        try (ByteArrayInputStream docxInputStream = new ByteArrayInputStream(docxBytes);
             ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {

            Document doc = new Document(docxInputStream);
            doc.saveToStream(pdfOutputStream, FileFormat.PDF);

            return pdfOutputStream.toByteArray();
        }
    }

    private byte[] replaceText(String initFilePath, Map<String, String> variables) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream(initFilePath);

        //  replace variables
        if (is != null) {
            XWPFDocument doc = new XWPFDocument(is);
            for (XWPFParagraph p : doc.getParagraphs()) {
                List<XWPFRun> runs = p.getRuns();
                if (runs != null) {
                    for (XWPFRun r : runs) {
                        String text = r.getText(0);
                        for (Map.Entry<String, String> m : variables.entrySet()) {
                            if (text != null && text.contains(m.getKey())) {
                                text = text.replace(m.getKey(), m.getValue());
                                r.setText(text, 0);
                            }
                        }
                    }
                }
            }
            // convert docs to byte[]
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            doc.close();

            byte[] xwpfDocumentBytes = out.toByteArray();
            out.close();

            return xwpfDocumentBytes;
        }
        return null;
    }

    private Map<String, String> updateVariables(Map<String, String> map, Collaborator collaborator) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        map.put("${collaboratorName}", collaborator.getFirstName().toUpperCase() + " " + collaborator.getLastName().toUpperCase());
        map.put("${collaboratorAddress}", collaborator.getAddress().toUpperCase());
        map.put("${date}", LocalDate.now().format(formatter));
        return map;
    }
}
