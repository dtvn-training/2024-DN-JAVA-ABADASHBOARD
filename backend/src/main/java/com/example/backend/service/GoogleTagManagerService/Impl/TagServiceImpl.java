package com.example.backend.service.GoogleTagManagerService.Impl;

import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.request.ParameterRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.entity.ParameterMaster;
import com.example.backend.entity.TemplateMaster;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.ParameterMapper;
import com.example.backend.repository.ParameterMasterRepository;
import com.example.backend.repository.TemplateMasterRepository;
import com.example.backend.service.GoogleTagManagerService.TagService;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.Parameter;
import com.google.api.services.tagmanager.model.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    @Value("${google-tag-manager.account_id}")
    private String accountId;
    private final TagManager tagManager;
    private final TemplateMasterRepository templateMasterRepository;
    private final ParameterMasterRepository parameterMasterRepository;
    private final ParameterMapper parameterMapper;


    @Override
    public ApiResponse<?> CreateTag(CreateTagRequest request) {
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
                    Optional<ParameterMaster> findParameterByKey= parameterMasterRepository.findByParameterKey(paraStr.getType());
                    Parameter parameter = getParameter(paraStr, findParameterByKey);
                    parameters.add(parameter);
                }catch (Exception e){
                    throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
                }
            }
            tag.setParameter(parameters);
            tag= tagManager.accounts().containers().workspaces().tags().create(parent,tag).execute();

            return ApiResponse.builder()
                    .code(200)
                    .message("Create tag successfully")
                    .data(List.of(tag))
                    .build();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMessage());
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
