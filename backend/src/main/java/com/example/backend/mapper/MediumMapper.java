package com.example.backend.mapper;

import com.example.backend.dto.MediumDto;
import com.example.backend.entity.Medium;
import org.springframework.stereotype.Component;

@Component
public class MediumMapper implements AbstractDefault<MediumDto, Medium>{
    @Override
    public MediumDto mapToDto(Medium entity) {
        return MediumDto.builder()
                .mediumId(entity.getMediumId())
                .mediumName(entity.getMediumName())
                .build();
    }

    @Override
    public Medium mapToEntity(MediumDto entity) {
        return Medium.builder()
                .mediumId(entity.getMediumId())
                .mediumName(entity.getMediumName())
                .build();
    }
}
