package com.example.staffmanagerapi.mapper;

import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.model.Society;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface SocietyMapper {

    SocietyDto societyToSocietyDto(Society society);
}
