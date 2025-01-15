package com.example.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class DropDownCampaignResponse {
    private Long campaignId;
    private String campaignName;
    public DropDownCampaignResponse(Long campaignId, String campaignName) {
        this.campaignId = campaignId;
        this.campaignName = campaignName;
    }
}
