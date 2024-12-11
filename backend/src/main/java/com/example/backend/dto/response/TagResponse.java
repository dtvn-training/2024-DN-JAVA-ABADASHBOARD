package com.example.backend.dto.response;

import com.example.backend.dto.ParameterDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponse {
    private Long tagId;
    private String tagName;
    private String type;
    private String status;
    private String accountId;
    private String containerId;
    private String workspaceId;
    private String tagFiringOption;
    private String monitoringMetadata;
    private List<ParameterDto> parameters;
}
