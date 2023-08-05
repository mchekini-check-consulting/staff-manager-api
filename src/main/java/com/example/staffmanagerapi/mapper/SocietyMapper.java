package com.example.staffmanagerapi.mapper;

import com.example.staffmanagerapi.dto.society.SocietyDto;
import com.example.staffmanagerapi.model.Society;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SocietyMapper {
  SocietyMapper INSTANCE = Mappers.getMapper(SocietyMapper.class);

  SocietyDto societyToSocietyDto(Society society);
}
