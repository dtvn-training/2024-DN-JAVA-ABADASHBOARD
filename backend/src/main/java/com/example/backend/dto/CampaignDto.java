package com.example.backend.dto;

import com.example.backend.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampaignDto {
    private Long campaignId;
    private String campaignName;
    private BigDecimal budget;
    private LocalDateTime campaignStartDate;
    private LocalDateTime campaignEndDate;
    private String targetAudience;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
}
