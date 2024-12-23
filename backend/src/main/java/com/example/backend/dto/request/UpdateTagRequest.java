package com.example.backend.dto.request;

import com.example.backend.dto.ParameterDto;
import com.example.backend.entity.ParameterMaster;
import com.example.backend.entity.TemplateMaster;
import com.example.backend.enums.TagStatus;
import com.google.api.services.tagmanager.model.TagConsentSetting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class UpdateTagRequest {
    @NotNull(message = "NOT_NULL")
    private Long tagId;
    @NotBlank(message = "NOT_BLANK")
    private String tagName;
    @NotNull(message = "NOT_NULL")
    private String type;
    @NotNull(message = "NOT_NULL")
    private TagStatus status;
    @NotNull(message = "NOT_NULL")
    private String accountId;
    @NotNull(message = "NOT_NULL")
    private String containerId;
    @NotNull(message = "NOT_NULL")
    private String workspaceId;
    @Nullable
    private String tagGtmId;
    @Nullable
    private String fingerPrint;
    @Nullable
    private String tagFiringOption;
    @Nullable
    private String monitoringMetadata;
    @Nullable
    private List<String> positiveTriggerId;
    @Nullable
    private List<String> blockingTriggerId;
    @Nullable
    private List<String> removeTriggerId;
    @Nullable
    private TagConsentSetting consentSetting;
    @Nullable
    Set<ParameterDto> parameterDtos;
}
