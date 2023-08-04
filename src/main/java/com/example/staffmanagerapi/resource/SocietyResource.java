package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.service.SocietyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/society")
public class SocietyResource {

  private final SocietyService societyService;

  public SocietyResource(SocietyService societyService) {
    this.societyService = societyService;
  }

  @PostMapping
  @Authenticated(authenticated = true, hasAnyRoles = {"admin"})
  public void createOrUpdateSocietyInfo(@RequestBody @Valid SocietyDto data) {
    this.societyService.createOrUpdateSocietyInfo(data);
  }
}
