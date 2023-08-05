package com.example.staffmanagerapi.resource;

import com.example.staffmanagerapi.aspect.authenticated.Authenticated;
import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.service.SocietyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("api/v1/society")
public class SocietyResource {

  private final SocietyService societyService;

  public SocietyResource(SocietyService societyService) {
    this.societyService = societyService;
  }

  @PostMapping
  @ResponseStatus(CREATED)
  @Authenticated(authenticated = true, hasAnyRoles = {"admin"})
  public void createSocietyInfo(@RequestBody @Valid SocietyDto data) {
    this.societyService.createSocietyInfo(data);
  }

}
