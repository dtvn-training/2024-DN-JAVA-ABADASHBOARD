package com.example.backend.service;

import com.example.backend.dto.MediumDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MediumService {
    List<MediumDto> getMediums();
}
