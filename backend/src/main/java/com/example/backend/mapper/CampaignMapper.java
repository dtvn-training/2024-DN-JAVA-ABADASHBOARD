package com.example.backend.mapper;

import com.example.backend.dto.CampaignDto;
import com.example.backend.entity.Campaign;
import org.springframework.stereotype.Component;

@Component
public class CampaignMapper implements AbstractDefault<CampaignDto, Campaign>{
    @Override
    public CampaignDto mapToDto(Campaign entity) {
        return CampaignDto.builder()
                .campaignId(entity.getCampaignId())
                .campaignName(entity.getCampaignName())
                .campaignStartDate(entity.getCampaignStartDate())
                .campaignEndDate(entity.getCampaignEndDate())
                .budget(entity.getBudget())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    @Override
    public Campaign mapToEntity(CampaignDto entity) {
        return Campaign.builder()
                .campaignId(entity.getCampaignId())
                .campaignName(entity.getCampaignName())
                .campaignStartDate(entity.getCampaignStartDate())
                .campaignEndDate(entity.getCampaignEndDate())
                .budget(entity.getBudget())
                .status(entity.getStatus())
                .build();
    }
}
