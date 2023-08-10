package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.mapper.SocietyMapper;
import com.example.staffmanagerapi.model.Society;
import com.example.staffmanagerapi.repository.SocietyRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SocietyService {

  private final SocietyRepository societyRepository;
  private final SocietyMapper societyMapper;

  public SocietyService(
    SocietyRepository societyRepository,
    SocietyMapper societyMapper
  ) {
    this.societyRepository = societyRepository;
    this.societyMapper = societyMapper;
  }

  public void createSocietyInfo(SocietyDto data) {
    List<Society> records = this.societyRepository.findAll();

    if (records.isEmpty()) {
      this.societyRepository.save(this.societyMapper.societyDtoToSociety(data));
    } else {
      throw new BadRequestException("Society already created");
    }
  }
}
