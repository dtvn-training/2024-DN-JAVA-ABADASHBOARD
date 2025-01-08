package com.example.backend.service;

import com.example.backend.dto.CampaignDto;
import com.example.backend.dto.response.DropDownCampaignResponse;
import com.example.backend.dto.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CampaignService {
    PageResponse<CampaignDto> getAllCampaigns(int pageNum, int pageSize);
    List<DropDownCampaignResponse> getAllDropDownCampaigns();
    CampaignDto getCampaignById(Long campaignId);
    PageResponse<CampaignDto> getAllCampaignsByFilter(int pageNum, int pageSize,
                                                      String status,
                                                      double minBudget,
                                                      double maxBudget,
                                                      String startDate, String endDate
                                                      );
}
