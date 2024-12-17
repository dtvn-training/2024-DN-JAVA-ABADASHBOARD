package com.example.backend.dto.response;

import com.example.backend.dto.ParameterDto;
import com.example.backend.enums.DeletedFlag;
import com.google.api.services.tagmanager.model.Parameter;
import com.google.api.services.tagmanager.model.TagConsentSetting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponse {
    private Long tagId;
    private String accountId;
    private String containerId;
    private String workspaceId;
    private TagConsentSetting consentSettings;
    private String consentStatus;
    private String fingerprint;
    private String name;
    private String path;
    private String tagGtmId;
    private String tagManagerUrl;
    private String type;
    private List<ParameterDto> parameters;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private DeletedFlag deletedFlag;

    public TagResponse(Long tagId, @NotNull(message = "NOT_NULL") String accountId, @NotNull(message = "NOT_NULL") String containerId, @NotNull(message = "NOT_NULL") String workspaceId, String s, String fingerPrint, @NotBlank(message = "NOT_BLANK") String tagName, String tagGtmId, @NotNull(message = "NOT_NULL") String type, List<ParameterDto> parameters, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, @NotNull(message = "NOT_NULL") DeletedFlag deletedFlag) {
        this.tagId = tagId;
        this.accountId = accountId;
        this.containerId = containerId;
        this.workspaceId = workspaceId;
//        this.consentSettings = consentSettings;
        this.consentStatus = consentStatus;
        this.fingerprint = fingerprint;
        this.name = tagName;
//        this.path = path;
        this.tagGtmId = tagGtmId;
//        this.tagManagerUrl = tagManagerUrl;
        this.type = type;
        this.parameters = parameters;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.deletedFlag = deletedFlag;
    }
}
