package com.example.backend.dto.response;

import com.example.backend.dto.ParameterDto;
import com.example.backend.enums.DeletedFlag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private DeletedFlag deletedFlag;
}
