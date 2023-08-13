package com.example.staffmanagerapi.service;

import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.exception.BadRequestException;
import com.example.staffmanagerapi.mapper.SocietyMapper;
import com.example.staffmanagerapi.model.Society;
import com.example.staffmanagerapi.repository.SocietyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocietyService {

    private final SocietyRepository societyRepository;
    private final SocietyMapper societyMapper;

    public SocietyService(SocietyRepository societyRepository, SocietyMapper societyMapper) {
        this.societyRepository = societyRepository;
        this.societyMapper = societyMapper;
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

    public SocietyDto getSocietyInformations() {
        Optional<Society> optionalSociety = societyRepository.findAll()
                .stream().findAny();

        if (optionalSociety.isPresent()) return SocietyDto.builder()
                .siret(optionalSociety.get().getSiret())
                .vat(optionalSociety.get().getVat())
                .name(optionalSociety.get().getName())
                .email(optionalSociety.get().getEmail())
                .address(optionalSociety.get().getAddress())
                .capital(optionalSociety.get().getCapital())
                .contact(optionalSociety.get().getContact())
                .build();

        throw new BadRequestException("Aucune société n'est présente en base");
    }
}
