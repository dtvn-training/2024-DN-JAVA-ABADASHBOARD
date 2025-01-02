package com.example.backend.mapper;


import com.example.backend.dto.ParameterDto;
import com.example.backend.entity.ParameterMaster;
import org.springframework.stereotype.Component;

@Component
public class ParameterMapper implements AbstractDefault<ParameterDto, ParameterMaster> {

    @Override
    public ParameterDto mapToDto(ParameterMaster entity) {
        return ParameterDto.builder()
                .key(entity.getParameterKey())
                .type(entity.getType())
                .build();
    }

    @Override
    public ParameterMaster mapToEntity(ParameterDto dto) {
        return ParameterMaster.builder()
                .parameterKey(dto.getKey())
                .type(dto.getType())
                .build();
    }
}
