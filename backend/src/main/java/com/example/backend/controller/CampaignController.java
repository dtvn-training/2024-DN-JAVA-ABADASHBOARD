package com.example.backend.controller;

import com.example.backend.comon.CreateApiResponse;
import com.example.backend.dto.CampaignDto;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.DropDownCampaignResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaign")
public class CampaignController {
    private final CampaignService campaignService;

    @GetMapping("/get-all-campaign")
    public ApiResponse<PageResponse<CampaignDto>> getCampaigns(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                            @RequestParam(value = "pageSize", defaultValue = "6") int pageSize
                                                                            ) {
        try{
            PageResponse<CampaignDto> response= campaignService.getAllCampaigns(pageNum, pageSize);
            return CreateApiResponse.createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),e.getMessage());
        }
    }

    @GetMapping("/get-campaign-by-id/{campaignId}")
    public ApiResponse<CampaignDto> getCampaignById(@PathVariable Long campaignId) {
        try{
            CampaignDto response= campaignService.getCampaignById(campaignId);
            return CreateApiResponse.createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),e.getMessage());
        }
    }

    @GetMapping("/get-dropdown-campaign")
    public ApiResponse<List<DropDownCampaignResponse>> getDropdownCampaign() {
        try{
            List<DropDownCampaignResponse> response= campaignService.getAllDropDownCampaigns();
            return CreateApiResponse.createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),e.getMessage());
        }
    }

    @GetMapping("/get-campaigns-by-filter")
    public ApiResponse<PageResponse<CampaignDto>> getCampaignsByFilters(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                                                        @RequestParam(value = "pageSize", defaultValue = "6") int pageSize,
                                                                        @RequestParam(value = "status", defaultValue = "ACTIVE") String status,
                                                                        @RequestParam(value = "minBudget", defaultValue = "0") double minBudget,
                                                                        @RequestParam(value = "maxBudget", defaultValue = "100") double maxBudget,
                                                                        @RequestParam(value = "startDate", defaultValue = "2024-12-30") String startDate,
                                                                        @RequestParam(value = "endDate", defaultValue = "2025-03-01") String endDate
                                                                        ) {
        try{
            PageResponse<CampaignDto> response= campaignService.getAllCampaignsByFilter(pageNum,pageSize,status,minBudget,maxBudget,startDate,endDate);
            return CreateApiResponse.createResponse(response);
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getStatusCode().value(),e.getMessage());
        }
    }
}
