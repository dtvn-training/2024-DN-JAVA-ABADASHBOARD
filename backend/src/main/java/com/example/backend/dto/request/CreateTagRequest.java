package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "containerId is required")
    private String containerId;
    @NotNull(message = "workspace is required")
    private String workspaceId;
    @NotNull(message = "parameter is required")
    private List<ParameterRequest> parameters;

}
