package com.example.backend.dto.request;

import com.example.backend.enums.TagStatus;
import com.google.api.services.tagmanager.model.TagConsentSetting;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateTagRequest {
    @NotNull(message = "tag name is required")
    private String tagName;
    @NotNull(message = "tag type is required")
    private String tagType;
    @Null
    private List<String> positiveTriggerId;
    @Null
    private List<String> blockingTriggerId;
    @Null
    private TagConsentSetting consentSetting;
    @NotNull(message = "containerId is required")
    private String containerId;
    @NotNull(message = "workspace is required")
    private String workspaceId;
    @NotNull(message = "status is required")
    private TagStatus status;
    @NotNull(message = "parameter is required")
    private List<ParameterRequest> parameters;
}
