package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.model.Society;
import com.example.staffmanagerapi.repository.SocietyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocietyService {

    private final SocietyRepository societyRepository;

    public SocietyService(SocietyRepository societyRepository) {
        this.societyRepository = societyRepository;
    }

    public void createSocietyInfo(SocietyDto data) {
        List<Society> records = this.societyRepository.findAll();

        if (records.isEmpty()) {
            this.societyRepository.save(
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
