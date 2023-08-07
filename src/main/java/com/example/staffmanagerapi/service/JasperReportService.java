package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.exception.MultipleSocietiesFoundException;
import com.example.staffmanagerapi.model.Society;
import com.example.staffmanagerapi.repository.SocietyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JasperReportService {

    private ActivityService activityService;
    private SocietyRepository societyRepository;
    @Autowired
    public JasperReportService(@Lazy ActivityService activityService, SocietyRepository societyRepository) {
        this.activityService = activityService;
        this.societyRepository = societyRepository;
    }

    public <T> byte[] generateReport(String reportClassPathLocation, List<T> data, Map<String,Object> extraParameters,String preferredPdfFileName) {

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

            // Save pdf in classpath::resources/reports/temp/ folder
            String tempFolderLocation = "src/main/resources/reports/temp/";
            File tempDirectory = new File(tempFolderLocation);
            if (!tempDirectory.exists()) {
                boolean created = tempDirectory.mkdirs(); // creer le dossier dans src/main/java/resources s'il existe pas
                if (!created) {
                    throw new JRRuntimeException("Cannot create temp directory for reports");
                }
            }
            String pdfLocation = tempFolderLocation+preferredPdfFileName+".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint,pdfLocation);
            // Export report as PDF
            byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
            log.info("Rapport qui existe dans {} est généré avec success !",reportClassPathLocation);
            log.info("Un raport vien d'étre généré sous le nom {} ",preferredPdfFileName+".pdf");
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
        // fetch society info and add them to default report params map
        List<Society> societies = this.societyRepository.findAll();
        if (societies.size() > 1) {
            throw new MultipleSocietiesFoundException("On s'attend à ce qu'une société soit renvoyée de la base, on en a trouvé plusieurs");
        } else if (societies.isEmpty()) {
            throw new EntityNotFoundException("Aucune Société en base, la génération de la facture client dépends du TVA de la société ainsi que d'autres informations");
        }
        Society society = societies.get(0); // there is only 1
        // add the image as a parameter
        // + other params
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("logo", ImageIO.read(imageInputStream));
        parameters.put("company-name", society.getName());
        parameters.put("company-tva", society.getVat());
        parameters.put("company-contact-name", society.getContact());
        parameters.put("company-contact-email", society.getEmail());
        parameters.put("company-adress", society.getAddress());
        parameters.put("company-capital", society.getCapital());
        parameters.put("company-siret", society.getSiret());

        // Add/combine extraParameters with parameters map
        parameters.putAll(extraParameters);
        if (!extraParameters.isEmpty()){
            log.info("Les paramètres en extra ajoutés dans le report sont {} ",extraParameters);
        }
        return parameters;
    }

}