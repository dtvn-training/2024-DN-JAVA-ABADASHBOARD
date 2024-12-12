package com.example.backend.mapper;

import com.example.backend.dto.TagDto;
import com.example.backend.entity.Tag;
import com.example.backend.entity.Trigger;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class TagMapper implements AbstractDefault<TagDto, Tag>{
    @Override
    public TagDto mapToDto(Tag entity) {
        List<String> triggerIds= entity.getTriggers().stream().map(Trigger::getTriggerGTMId).toList();
        return TagDto.builder()
                .tagId(entity.getTagId())
                .tagGtmId(entity.getTagGtmId())
                .tagName(entity.getTagName())
                .type(entity.getType())
                .status(entity.getStatus())
                .accountId(entity.getAccountId())
                .containerId(entity.getContainerId())
                .fingerPrint(entity.getFingerPrint())
                .workspaceId(entity.getWorkspaceId())
                .tagFiringOption(entity.getTagFiringOption())
                .monitoringMetadata(entity.getMonitoringMetadata())
                .positiveTriggerId(triggerIds)
                .templateMasters(entity.getTemplateMasters())
                .parameterMasters(entity.getParameterMasters())
                .build();
    }

    @Override
    public Tag mapToEntity(TagDto dto) {
        return Tag.builder()
                .tagId(dto.getTagId())
                .tagGtmId(dto.getTagGtmId())
                .tagName(dto.getTagName())
                .type(dto.getType())
                .status(dto.getStatus())
                .accountId(dto.getAccountId())
                .containerId(dto.getContainerId())
                .fingerPrint(dto.getFingerPrint())
                .workspaceId(dto.getWorkspaceId())
                .tagFiringOption(dto.getTagFiringOption())
                .monitoringMetadata(dto.getMonitoringMetadata())
                .parameterMasters(dto.getParameterMasters())
                .templateMasters(dto.getTemplateMasters())
                .build();
    }
}
