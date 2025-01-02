package com.example.backend.mapper;

import com.example.backend.dto.response.TriggerTemplateResponse;
import com.example.backend.entity.TriggerTemplate;
import org.springframework.stereotype.Component;

@Component
public class TriggerTemplateMapper {

    public TriggerTemplateResponse convertEntityToResponse(TriggerTemplate triggerTemplate) {
        if (triggerTemplate == null) {
            return null;
        }
        return TriggerTemplateResponse.builder()
                .triggerTemplateId(triggerTemplate.getTriggerTemplateId())
                .key(triggerTemplate.getKey())
                .displayName(triggerTemplate.getDisplayName())
                .vendorTemplatePublicId(triggerTemplate.getVendorTemplatePublicId())
                .groupDisplayName(triggerTemplate.getGroupDisplayName())
                .groupDisplayNameAllEvents(triggerTemplate.getGroupDisplayNameAllEvents())
                .groupDisplayNameSomeEvents(triggerTemplate.getGroupDisplayNameSomeEvents())
                .build();
    }
}