package com.example.backend.mapper;

import com.example.backend.dto.ParameterDto;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TagMapper{
    public TagResponse convertEntityToTagResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new TagResponse(
                tag.getTagId(),
                tag.getAccountId(),
                tag.getContainerId(),
                tag.getWorkspaceId(),
                null,
                tag.getStatus() != null ? tag.getStatus().name() : null,
                tag.getFingerPrint(),
                tag.getTagName(),
                tag.getTagGtmId(),
                tag.getType(),
                tag.getParameterMasters() != null ? tag.getParameterMasters()
                        .stream()
                        .map(tagMap-> ParameterDto.builder()
                                .key(tagMap.getParameterKey())
                                .type(tagMap.getType())
                                .build())
                        .collect(Collectors.toList()) : null,
                tag.getCreatedAt(),
                tag.getUpdatedAt(),
                tag.getCreatedBy(),
                tag.getUpdatedBy(),
                tag.getDeletedFlag()
        );
    }
}
