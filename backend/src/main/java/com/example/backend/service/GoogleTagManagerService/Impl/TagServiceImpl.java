package com.example.backend.service.GoogleTagManagerService.Impl;

import com.example.backend.dto.ParameterDto;
import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.request.ParameterRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.ParameterMaster;
import com.example.backend.entity.TemplateMaster;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.TagStatus;
import com.example.backend.exception.ApiException;
import com.example.backend.repository.ParameterMasterRepository;
import com.example.backend.repository.TagRepository;
import com.example.backend.repository.TemplateMasterRepository;
import com.example.backend.service.GoogleTagManagerService.TagService;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.Parameter;
import com.google.api.services.tagmanager.model.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    @Value("${google-tag-manager.account_id}")
    private String accountId;
    private final TagManager tagManager;
    private final TemplateMasterRepository templateMasterRepository;
    private final ParameterMasterRepository parameterMasterRepository;
    private final TagRepository tagRepository;


    private TagResponse covertTagToTagResponse(Tag tag) {
        List<ParameterDto> parameterDtos = new ArrayList<>();
        for(Parameter parameter: tag.getParameter()){
            ParameterDto parameterDto= ParameterDto.builder()
                    .key(parameter.getKey())
                    .type(parameter.getType())
                    .build();
            parameterDtos.add(parameterDto);
        }
        return TagResponse.builder()
                .tagId(tag.getTagId())
                .accountId(tag.getAccountId())
                .containerId(tag.getContainerId())
                .workspaceId(tag.getWorkspaceId())
                .consentSettings(tag.getConsentSettings())
                .tagManagerUrl(tag.getTagManagerUrl())
                .fingerprint(tag.getFingerprint())
                .name(tag.getName())
                .type(tag.getType())
                .parameters(parameterDtos)
                .path(tag.getPath())
                .build();
    }

    private <T> ApiResponse<T> createResponse(Class<T> clazz, int code, String message, List<T> data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    private void saveTag(Tag tag, TemplateMaster templateMaster, CreateTagRequest request){
        Set<ParameterMaster> parameterMasters= new LinkedHashSet<>();
        Set<TemplateMaster> templateMasterSet = new LinkedHashSet<>();
        for (Parameter parameter: tag.getParameter()){
            ParameterMaster parameterMaster= ParameterMaster.builder()
                    .parameterKey(parameter.getKey())
                    .type(parameter.getType())
                    .build();
            parameterMasters.add(parameterMaster);
        }
        templateMasterSet.add(templateMaster);
        com.example.backend.entity.Tag entity= com.example.backend.entity.Tag.builder()
                .tagId(tag.getTagId()!=null?Long.parseLong(tag.getTagId()):null)
                .accountId(tag.getAccountId()!=null?tag.getAccountId():accountId)
                .containerId(tag.getContainerId()!=null?tag.getContainerId(): request.getContainerId())
                .workspaceId(tag.getWorkspaceId()!=null?tag.getWorkspaceId(): request.getWorkspaceId())
                .tagName(tag.getName())
                .type(tag.getType())
                .fingerPrint(tag.getFingerprint())
                .monitoringMetadata(tag.getMonitoringMetadataTagNameKey())
                .parameterMasters(parameterMasters)
                .templateMasters(templateMasterSet)
                .status(request.getStatus())
                .build();
        tagRepository.save(entity);
    }

    @Override
    public ApiResponse<TagResponse> CreateTag(CreateTagRequest request) {
        if (request.getTagName() == null || request.getTagName().isEmpty() || request.getTagType() == null || request.getTagType().isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Tag name or tag type cannot be null or empty");
        }
        try {
            Optional<TemplateMaster> findTemplateByType= templateMasterRepository.findByType(request.getTagType());
            if(findTemplateByType.isEmpty()){
                throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Template type " + request.getTagType() + " does not exist");
            }
            TemplateMaster templateMaster = findTemplateByType.get();
            String parent = String.format("accounts/%s/containers/%s/workspaces/%s", accountId, request.getContainerId(),request.getWorkspaceId());
            Tag tag = new Tag();
            tag.setName(request.getTagName());
            tag.setType(templateMaster.getType());
            List<Parameter> parameters= new ArrayList<>();
            for(ParameterRequest paraStr : request.getParameters()){
                try{
                    Optional<ParameterMaster> findParameterByKey= parameterMasterRepository.findByParameterKey(paraStr.getKey());
                    Parameter parameter = getParameter(paraStr, findParameterByKey);
                    parameters.add(parameter);
                }catch (Exception e){
                    throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
                }
            }
            tag.setParameter(parameters);
            if(request.getPositiveTriggerId() != null) tag.setFiringTriggerId(request.getPositiveTriggerId());
            if(request.getConsentSetting()!=null) tag.setConsentSettings(request.getConsentSetting());
            if(TagStatus.SAVE_AND_PUSH.equals(request.getStatus())){
                tag= tagManager.accounts().containers().workspaces().tags().create(parent,tag).execute();
            }
            saveTag(tag, templateMaster, request);
            TagResponse convertCreateTagRes= covertTagToTagResponse(tag);
            List<TagResponse> createTagResponses= List.of(convertCreateTagRes);
            return createResponse(TagResponse.class, 200, "Create tag success", createTagResponses);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @NotNull
    private static Parameter getParameter(ParameterRequest paraStr, Optional<ParameterMaster> findParameterByKey) {
        if(findParameterByKey.isEmpty()){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Parameter key " + paraStr + " does not exist");
        }
        ParameterMaster parameterMaster = findParameterByKey.get();
        Parameter parameter = new Parameter();
        parameter.setKey(parameterMaster.getParameterKey());
        parameter.setType(parameterMaster.getType());
        parameter.setValue(paraStr.getValue());
        return parameter;
    }
}
