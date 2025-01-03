package com.example.backend.dto;

import com.example.backend.entity.ParameterMaster;
import com.example.backend.entity.TemplateMaster;
import com.example.backend.entity.Trigger;
import com.example.backend.enums.TagStatus;
import com.google.api.services.tagmanager.model.TagConsentSetting;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TagDto {
    @NotNull(message = "NOT_NULL")
    private Long tagId;
    @Null
    private String tagGtmId;
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
    @Null
    private String fingerPrint;
    @NotNull(message = "NOT_NULL")
    private String workspaceId;
    @Null
    private String tagFiringOption;
    @Null
    private String monitoringMetadata;
    @Null
    private List<String> positiveTriggerId;
    @Null
    private List<String> blockingTriggerId;
    @Null
    private TagConsentSetting consentSetting;
    @NotNull(message = "NOT_NULL")
    Set<TemplateMaster> templateMasters;
    @NotNull(message = "NOT_NULL")
    Set<ParameterMaster> parameterMasters;
}
