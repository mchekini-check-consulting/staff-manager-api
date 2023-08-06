package com.example.staffmanagerapi.service;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JasperReportService {

    ActivityService activityService;

    @Autowired
    public JasperReportService(ActivityService activityService) {
        this.activityService = activityService;
    }

    public <T> byte[] generateReport(String reportClassPathLocation, List<T> data, Map<String,Object> extraParameters) {

        try {
            log.info("Tentative de géneration du rapport {} contenant {} lignes ... ", reportClassPathLocation, data.size());
            // Load the Jasper Report design file
            Resource reportResource = new ClassPathResource(reportClassPathLocation);
            InputStream reportInputStream = reportResource.getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportInputStream); // compiler le report, c'est la version java du fichier .jrxml

            // Data source, comes from a service or repository ... or mock data
            JRDataSource dataSource = new JRBeanCollectionDataSource(data);

            Map<String, Object> parameters = generateReportParameters(extraParameters);
            // Generate the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export report as PDF
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            log.info("Rapport qui existe dans {} est généré avec success !",reportClassPathLocation);
            return pdf;
        } catch (IOException e) {
            log.error("Erreur lors de la generation du rapport, causes possibles : Image introuvable dans classpath, fichier {}.jrxml introuvable ", reportClassPathLocation);
            log.error("Erreur lors de la génération du rapport. Caused by: {}", e.getMessage(), e);
        } catch (JRException e) {
            log.error("Erreur lors de la generation du rapport, causes possibles : compilation du rapport, creation de datasource du rapport ou conversion en PDF du rapport");
            log.error("Erreur lors de la génération du rapport. Caused by: {}", e.getMessage(), e);
        }
        throw new RuntimeException();
    }

    public Map<String, Object> generateReportParameters(Map<String,Object> extraParameters) throws IOException {
        // Fetch the logo from the classpath
        Resource imageResource = new ClassPathResource("reports/assets/logo.png");
        InputStream imageInputStream = imageResource.getInputStream();

        // Create parameters map and add the image as a parameter
        // + other params
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", ImageIO.read(imageInputStream));
        parameters.put("company-name", "Check Consulting"); // j'ai écris sa en dur, mais on va le remplacer par les données en base.
        parameters.put("company-tva", 19);
        parameters.put("company-contact-name", "Chekini");
        parameters.put("company-contact-email", "mchekini@checkconsulting.fr");
        parameters.put("company-adress", "16 rue de la Voute, Paris 75012");
        parameters.put("company-capital", "12345 £");
        parameters.put("company-siret", "123 568 941 00056");

        return parameters;
    }

}