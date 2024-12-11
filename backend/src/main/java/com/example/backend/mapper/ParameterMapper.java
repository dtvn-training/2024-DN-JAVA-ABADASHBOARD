package com.example.backend.mapper;

import com.example.backend.dto.ParameterDto;
import org.mapstruct.Mapper;

import java.lang.reflect.Parameter;

@Mapper(componentModel = "spring")
public interface ParameterMapper {
    ParameterDto toParameterDto(Parameter parameter);
}