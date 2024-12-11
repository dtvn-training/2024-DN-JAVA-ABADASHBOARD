package com.example.backend.mapper;

import com.example.backend.dto.ParameterDto;
import com.example.backend.entity.ParameterMaster;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParameterMapper {
    ParameterDto toParameterDto(ParameterMaster parameterMaster);

    List<ParameterDto> toParameterDtoList(List<ParameterMaster> parameterMasters);
}