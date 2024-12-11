package com.example.backend.dto.response;

import com.example.backend.dto.ParameterDto;
import com.google.api.services.tagmanager.model.TagConsentSetting;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponse {
    private String accountId;
    private String containerId;
    private String workspaceId;
    private TagConsentSetting consentSettings;
    private String consentStatus;
    private String fingerprint;
    private String name;
    private String path;
    private String tagId;
    private String tagManagerUrl;
    private String type;
    private List<ParameterDto> parameters;
}
