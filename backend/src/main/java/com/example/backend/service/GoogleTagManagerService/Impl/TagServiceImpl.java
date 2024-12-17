package com.example.backend.service.GoogleTagManagerService.Impl;

import com.example.backend.dto.ParameterDto;
import com.example.backend.dto.TagDto;
import com.example.backend.dto.request.CreateTagRequest;
import com.example.backend.dto.request.ListTagRequestGTM;
import com.example.backend.dto.request.ParameterRequest;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.ParameterMaster;
import com.example.backend.entity.TemplateMaster;
import com.example.backend.entity.Trigger;
import com.example.backend.enums.DeletedFlag;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.TagStatus;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.*;
import com.example.backend.service.GoogleTagManagerService.TagService;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.ListTagsResponse;
import com.google.api.services.tagmanager.model.Parameter;
//import com.google.api.services.tagmanager.model.Tag;
import com.example.backend.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private final TagMapper tagMapper;
    private final TriggerRepository triggerRepository;
    private final TriggerTemplateRepository triggerTemplateRepository;


    private TagResponse covertTagToTagResponse(com.google.api.services.tagmanager.model.Tag tag,CreateTagRequest request) {
        List<ParameterDto> parameterDtos = new ArrayList<>();
        for(Parameter parameter: tag.getParameter()){
            ParameterDto parameterDto= ParameterDto.builder()
                    .key(parameter.getKey())
                    .type(parameter.getType())
                    .build();
            parameterDtos.add(parameterDto);
        }
        return TagResponse.builder()
                .tagGtmId(tag.getTagId())
                .accountId(tag.getAccountId()!=null?tag.getAccountId():accountId)
                .containerId(tag.getContainerId()!=null?tag.getContainerId():request.getContainerId())
                .workspaceId(tag.getWorkspaceId()!=null?tag.getWorkspaceId():request.getWorkspaceId())
                .consentSettings(tag.getConsentSettings())
                .tagManagerUrl(tag.getTagManagerUrl())
                .fingerprint(tag.getFingerprint())
                .name(tag.getName())
                .type(tag.getType())
                .parameters(parameterDtos)
                .path(tag.getPath())
                .build();
    }

    private <T> ApiResponse<T> createResponse(Class<T> clazz, int code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    private Tag saveTag(com.google.api.services.tagmanager.model.Tag tag,
                                                   CreateTagRequest request,
                                                   TemplateMaster templateMaster,
                                                   Set<ParameterMaster> parameterMasters,
                                                   Set<Trigger> triggers
    ){
        Set<TemplateMaster> templateMasterSet = new LinkedHashSet<>();
        templateMasterSet.add(templateMaster);
        Tag entity= Tag.builder()
                .tagGtmId(tag.getTagId())
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
        entity.setDeletedFlag(DeletedFlag.ACTIVE);
        entity.setCreatedBy("Hieu");
        if(!triggers.isEmpty()){
            entity.setTriggers(triggers);
        }
        return tagRepository.save(entity);
    }

    private Trigger checkTriggerValid(String triggerId){
        try{
            return triggerRepository.findByTriggerGTMId(triggerId).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST.getCode(), "TriggerId not exist"));
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    private PageResponse<TagResponse> createPageResponse(Page<Tag> pageData, List<TagResponse> tagResponses) {
        return PageResponse.<TagResponse>builder()
                .currentPage(pageData.getNumber() + 1)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(tagResponses)
                .build();
    }

    @Override
    public ApiResponse<TagResponse> CreateTag(CreateTagRequest request) {
        if (request.getTagName() == null || request.getTagName().isEmpty() || request.getTagType() == null || request.getTagType().isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Tag name or tag type cannot be null or empty");
        }
        try {
            Optional<com.example.backend.entity.Tag> findTagNameIsExist= tagRepository.findByTagName(request.getTagName());
            if(findTagNameIsExist.isPresent()){
                throw new ApiException(ErrorCode.CREATE_TAG_EXIST.getCode(), ErrorCode.CREATE_TAG_EXIST.getMessage());
            }
            Optional<TemplateMaster> findTemplateByType= templateMasterRepository.findByType(request.getTagType());
            if(findTemplateByType.isEmpty()){
                throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Template type " + request.getTagType() + " does not exist");
            }
            TemplateMaster templateMaster = findTemplateByType.get();
            String parent = String.format("accounts/%s/containers/%s/workspaces/%s", accountId, request.getContainerId(),request.getWorkspaceId());
            com.google.api.services.tagmanager.model.Tag tag = new com.google.api.services.tagmanager.model.Tag();
            tag.setName(request.getTagName());
            tag.setType(templateMaster.getType()); List<Parameter> parameters= new ArrayList<>();
            Set<ParameterMaster> parameterMasters= new LinkedHashSet<>();
            for(ParameterRequest paraStr : request.getParameters()){
                try{
                    Optional<ParameterMaster> findParameterByKey= parameterMasterRepository.findByParameterKey(paraStr.getKey());
                    if(findParameterByKey.isEmpty()){
                        throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Parameter key " + paraStr + " does not exist");
                    }
                    Parameter parameter = getParameter(paraStr, findParameterByKey);
                    parameters.add(parameter);
                    parameterMasters.add(findParameterByKey.get());
                }catch (Exception e){
                    throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
                }
            }
            tag.setParameter(parameters);
            Set<Trigger> triggers= new LinkedHashSet<>();
            if(request.getPositiveTriggerId() != null && !request.getPositiveTriggerId().isEmpty()){
                for(String triggerId : request.getPositiveTriggerId()){
                    Trigger checkTriggerExist= checkTriggerValid(triggerId);
                    triggers.add(checkTriggerExist);
                }
                tag.setFiringTriggerId(request.getPositiveTriggerId());
            }

            if(request.getBlockingTriggerId() != null && !request.getBlockingTriggerId().isEmpty()){
                for(String triggerId : request.getBlockingTriggerId()){
                    Trigger checkTriggerExist= checkTriggerValid(triggerId);
                    triggers.add(checkTriggerExist);
                }
                tag.setFiringTriggerId(request.getPositiveTriggerId());
            }
            if(request.getConsentSetting()!=null) tag.setConsentSettings(request.getConsentSetting());
            if(TagStatus.SAVE_AND_PUSH.equals(request.getStatus())){
                tag= tagManager.accounts().containers().workspaces().tags().create(parent,tag).execute();
            }
            Tag tagEntity=saveTag(tag,request, templateMaster, parameterMasters,triggers);
            TagResponse convertCreateTagRes= covertTagToTagResponse(tag,request);
            convertCreateTagRes.setTagId(tagEntity.getTagId());
            return createResponse(TagResponse.class, 200, "Create tag success", convertCreateTagRes);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @Override
    public ApiResponse<?> updatePushTagOnGTM(TagDto tagDto) {
        try{
            Optional<com.example.backend.entity.Tag> findTagExist= tagRepository.findByTagName(tagDto.getTagName());
            if(findTagExist.isPresent()){
                com.example.backend.entity.Tag findTag= findTagExist.get();
//                updateTag= tagMapper.mapToEntity(tagDto);
                findTag= tagMapper.convertDtoToEntity(tagDto,findTag);
                Set<Trigger> filterTrigger= findTag.getTriggers();

            }
            return null;
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    @Override
    public List<com.google.api.services.tagmanager.model.Tag> listTagGTM(ListTagRequestGTM requestGTM) throws IOException {
        String parent = String.format("accounts/%s/containers/%s/workspaces/%s", accountId, requestGTM.getContainerId(),
                requestGTM.getWorkspaceId());
        TagManager.Accounts.Containers.Workspaces.Tags.List request = tagManager.accounts().containers().workspaces()
                .tags().list(parent);
        ListTagsResponse response = request.execute();

        if (response != null && response.getTag() != null) {
            return response.getTag();
        } else {
            return null;
        }
    }

    @Override
    public PageResponse<TagResponse> listTags(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        // Map the Tag entities to TagResponse DTOs
        List<TagResponse> tagResponses = tagPage.getContent().stream()
                .map(tagMapper::convertEntityToTagResponse)
                .collect(Collectors.toList());

        return createPageResponse(tagPage, tagResponses);
    }

    @Override
    public TagResponse getTagById(Long id) {
        // Create a Pageable object for pagination
       return null;
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
