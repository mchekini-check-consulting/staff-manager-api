package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.model.Society;
import com.example.staffmanagerapi.repository.SocietyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SocietyService {

  private final SocietyRepository societyRepository;

  public SocietyService(SocietyRepository societyRepository) {
    this.societyRepository = societyRepository;
  }

  public Society createOrUpdateSocietyInfo(SocietyDto data) {
    List<Society> records = this.societyRepository.findAll();

    if (records.isEmpty()) {
      return this.societyRepository.save(
          Society
            .builder()
            .name(data.getName())
            .siret(data.getSiret())
            .vat(data.getVat())
            .contact(data.getContact())
            .email(data.getEmail())
            .address(data.getAddress())
            .capital(data.getCapital())
            .build()
        );
    } else {
      throw new BadRequestException("Society already created");
    }
  }
}
