package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.activity.in.CreateActivityInDto;
import com.example.staffmanagerapi.dto.activity.out.CompteRenduActiviteOutDto;
import com.example.staffmanagerapi.model.User;
import com.example.staffmanagerapi.service.ActivityService;
import com.example.staffmanagerapi.service.JasperReportService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("api/v1/activity")
@Slf4j
public class ActivityResource {

    private final ActivityService activityService;
    JasperReportService jasperReportService;
    private final User user;

    public ActivityResource(ActivityService activityService, User user,JasperReportService jasperReportService) {
        this.activityService = activityService;
        this.user = user;
        this.jasperReportService = jasperReportService;
    }

    @GetMapping
    @Authenticated(authenticated = true, hasAnyRoles = {"admin"})
    public ResponseEntity<List<CompteRenduActiviteOutDto>> getCurrentMonthCRA() {
        return ResponseEntity.ok(this.activityService.getCurrentMonthCRA());
    }

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    //@Authenticated(authenticated = true, hasAnyRoles = {"admin"})
    public <T> ResponseEntity<byte[]> generatePDFReport() {
        try {
            List<CompteRenduActiviteOutDto> data = this.activityService.getCurrentMonthCRA();
            byte[] pdfBytes = jasperReportService.generateReport("reports/cra.jrxml", data);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "compte-rendu-activité-"+ YearMonth.now() +".pdf");

            // Return the PDF in the response body
            return ResponseEntity.ok().headers(headers).body(pdfBytes);
        } catch (Exception e) {
            // Handle exceptions, return an error response, or log the error
            log.error("Une erreur est survenues lors de la generation du report des comptes-rendu-d'activite en PDF !");
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping
    @Authenticated(authenticated = true, hasAnyRoles = {"collab"})
    public ResponseEntity<?> createActivities(
            @RequestBody @Valid CreateActivityInDto data
    ) {
        this.activityService.createActivities(user, data.getActivities());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }
}
