package com.example.backend.controller;

import com.example.backend.comon.CreateApiResponse;
import com.example.backend.dto.CampaignDto;
import com.example.backend.dto.MediumDto;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.MediumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/medium")
public class MediumController {
    private final MediumService mediumService;

    @GetMapping("/get-dropdown-medium")
    public ApiResponse<List<MediumDto>> getMediums() {
        try {
            List<MediumDto> response = mediumService.getMediums();
            return CreateApiResponse.createResponse(response);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(), e.getMessage());
        }
    }
}
