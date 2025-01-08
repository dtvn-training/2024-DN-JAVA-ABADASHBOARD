package com.example.backend.service.Impl;

import com.example.backend.dto.MediumDto;
import com.example.backend.entity.Medium;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.MediumMapper;
import com.example.backend.repository.MediumRepository;
import com.example.backend.service.MediumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediumServiceImpl implements MediumService {
    private final MediumRepository mediumRepository;
    private final MediumMapper mediumMapper;
    @Override
    public List<MediumDto> getMediums() {
        try{
            List<Medium> mediums = mediumRepository.findAll();
            return mediums.stream().map(this.mediumMapper::mapToDto).toList();
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),e.getMessage());
        }
    }
}
