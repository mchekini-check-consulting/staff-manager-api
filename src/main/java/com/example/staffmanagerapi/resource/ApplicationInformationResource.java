package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.model.AppInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/app")
@Slf4j
public class ApplicationInformationResource {


    private final AppInformation appInformation;
    @Value("${kubeVersion}")
    private String kubeVersion;

    @Value("${POD_NAME}")
    private String podName;

    public ApplicationInformationResource(AppInformation appInformation) {
        this.appInformation = appInformation;
    }


    @GetMapping("details")
    public AppInformation getAppInformations() {
        log.info("application name = {}, version = {}, podName = {}", appInformation.getName(), appInformation.getVersion(), podName);
        return AppInformation.builder()
                .version(appInformation.getVersion())
                .name(appInformation.getName())
                .kubeVersion(kubeVersion)
                .podName(podName)
                .build();
    }


}
