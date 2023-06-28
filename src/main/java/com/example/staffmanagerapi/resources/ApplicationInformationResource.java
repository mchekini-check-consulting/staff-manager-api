package com.example.staffmanagerapi.resources;

import com.example.staffmanagerapi.model.AppInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/app")
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
