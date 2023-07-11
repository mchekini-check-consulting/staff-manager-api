package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.model.AppInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/app",produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ApplicationInformationResource {


    private final AppInformation appInformation;

    public ApplicationInformationResource(AppInformation appInformation) {
        this.appInformation = appInformation;
    }


    @GetMapping("details")
    public AppInformation getAppInformations() {
        return AppInformation.builder()
                .version(appInformation.getVersion())
                .name(appInformation.getName())
                .build();
    }


}
