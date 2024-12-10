package com.example.backend.dto.response;

import com.example.backend.dto.ParameterDto;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTagResponse{
    private String accountId;
    private String containerId;
    private String workspaceId;
    private Objects consentSettings;
    private String consentStatus;
    private String fingerprint;
    private String name;
    private String path;
    private String tagId;
    private String tagManagerUrl;
    private String type;
    private List<ParameterDto> parameters;
}
