package com.example.backend.service.Impl;

import com.example.backend.dto.ParameterDto;
import com.example.backend.dto.request.*;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.*;
import com.example.backend.enums.DeletedFlag;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.TagStatus;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.*;
import com.example.backend.service.TagService;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.ListTagsResponse;
import com.google.api.services.tagmanager.model.Parameter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
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
    private final TagTriggerRepository tagTriggerRepository;
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
                                                   Set<ParameterMaster> parameterMasters
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
        return tagRepository.save(entity);
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

    private com.google.api.services.tagmanager.model.Tag createFiringTriggerId(List<TagTrigger> tagTriggerList,
                                                                               List<String> firingTriggerId,
                                                                               com.google.api.services.tagmanager.model.Tag tag,
                                                                               @Nullable Tag findTag
    ) {
        if(firingTriggerId!= null && !firingTriggerId.isEmpty()){
            for(String triggerId : firingTriggerId){
                Trigger findTrigger= triggerRepository.findByTriggerGTMId(triggerId).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Trigger " + triggerId + " does not exist"));
                TagTrigger tagTrigger = new TagTrigger();
                tagTrigger.setTrigger(findTrigger);
                if(findTag!=null && findTag.getTagTriggers().contains(tagTrigger)){
                    continue;
                }
                tagTrigger.setPositive(true);
                tagTriggerList.add(tagTrigger);
            }
            tag.setFiringTriggerId(firingTriggerId);
        }
        return tag;
    }

    private com.google.api.services.tagmanager.model.Tag createBlockingTriggerId(List<TagTrigger> tagTriggerList,
                                                                               List<String> blockingTriggerId,
                                                                               com.google.api.services.tagmanager.model.Tag tag,
                                                                               @Nullable Tag findTag
    ) {
        if(blockingTriggerId!= null && !blockingTriggerId.isEmpty()){
            for(String triggerId : blockingTriggerId){
                Trigger findTrigger= triggerRepository.findByTriggerGTMId(triggerId).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Trigger " + triggerId + " does not exist"));
                TagTrigger tagTrigger = new TagTrigger();
                tagTrigger.setTrigger(findTrigger);
                if(findTag!=null && findTag.getTagTriggers().contains(tagTrigger)){
                    continue;
                }
                tagTrigger.setPositive(false);
                tagTriggerList.add(tagTrigger);
            }
            tag.setFiringTriggerId(blockingTriggerId);
        }
        return tag;
    }

    private com.google.api.services.tagmanager.model.Tag getTagOnGTM(String accountId, String containerId, String workspaceId,String tagId){
        try{
            String parent = String.format("accounts/%s/containers/%s/workspaces/%s/tags/%s", accountId, containerId,workspaceId,tagId);
            return tagManager.accounts().containers().workspaces().tags().get(parent).execute();
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Unable to get tag on GTM");
        }
    }

    private void removeTriggerFromGTM(com.google.api.services.tagmanager.model.Tag tag, List<String> triggerIds){
        if (!triggerIds.isEmpty()) {
            List<String> firingTriggerIds = new ArrayList<>(tag.getFiringTriggerId());
            firingTriggerIds.removeAll(triggerIds);
            tag.setFiringTriggerId(firingTriggerIds);

            List<String> blockingTriggerIds = new ArrayList<>(tag.getBlockingTriggerId());
            blockingTriggerIds.removeAll(triggerIds);
            tag.setBlockingTriggerId(blockingTriggerIds);
        }
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
            if(request.getConsentSetting()!=null) tag.setConsentSettings(request.getConsentSetting());
            List<TagTrigger> tagTriggers= new ArrayList<>();
            tag= createFiringTriggerId(tagTriggers,request.getPositiveTriggerId(),tag,null);
            tag= createBlockingTriggerId(tagTriggers,request.getBlockingTriggerId(),tag,null);
//            if(request.getPositiveTriggerId() != null && !request.getPositiveTriggerId().isEmpty()){
//                for(String triggerId : request.getPositiveTriggerId()){
//                    Trigger getTrigger= triggerRepository.findByTriggerGTMId(triggerId).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Trigger " + triggerId + " does not exist"));
//                    TagTrigger tagTrigger = new TagTrigger();
//                    tagTrigger.setTrigger(getTrigger);
//                    tagTrigger.setPositive(true);
//                    tagTriggers.add(tagTrigger);
//                }
//                tag.setFiringTriggerId(request.getPositiveTriggerId());
//            }
//            if(request.getBlockingTriggerId() != null && !request.getBlockingTriggerId().isEmpty()){
//                for(String triggerId : request.getBlockingTriggerId()){
//                    Trigger getTrigger= triggerRepository.findByTriggerGTMId(triggerId).orElseThrow(()->new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Trigger " + triggerId + " does not exist"));
//                    TagTrigger tagTrigger = new TagTrigger();
//                    tagTrigger.setTrigger(getTrigger);
//                    tagTrigger.setPositive(false);
//                    tagTriggers.add(tagTrigger);
//                }
//                tag.setBlockingTriggerId(request.getBlockingTriggerId());
//            }
            if(TagStatus.SAVE_AND_PUSH.equals(request.getStatus())){
                tag= tagManager.accounts().containers().workspaces().tags().create(parent,tag).execute();
            }
            Tag tagEntity=saveTag(tag,request, templateMaster, parameterMasters);
            for(TagTrigger tagTrigger : tagTriggers){
                tagTrigger.setTag(tagEntity);
                tagTriggerRepository.save(tagTrigger);
            }
            TagResponse convertCreateTagRes= covertTagToTagResponse(tag,request);
            convertCreateTagRes.setTagId(tagEntity.getTagId());
            return createResponse(TagResponse.class, 200, "Create tag success", convertCreateTagRes);
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), e.getMessage());
        }
    }

    @Override
    @Transactional
    public ApiResponse<?> updatePushTagOnGTM(UpdateTagRequest request) {
        try{
            Optional<Tag> findTagExist= tagRepository.findByTagName(request.getTagName());
            if(findTagExist.isPresent()){
                Tag findTag= findTagExist.get();
                com.google.api.services.tagmanager.model.Tag tag;
                if(request.getTagGtmId()!=null){
                    tag= getTagOnGTM(request.getAccountId(),request.getContainerId(),request.getWorkspaceId(),request.getTagGtmId());
                }else{
                    tag= new com.google.api.services.tagmanager.model.Tag();
                }
                tag.setName(findTag.getTagName());
                tag.setType(findTag.getType());
                List<TagTrigger> tagTriggers= new ArrayList<>();
                tag=createFiringTriggerId(tagTriggers,request.getPositiveTriggerId(),tag,findTag);
                tag=createBlockingTriggerId(tagTriggers,request.getBlockingTriggerId(),tag,findTag);
                if(request.getRemoveTriggerId()!=null && !request.getRemoveTriggerId().isEmpty()){
                    for(String triggerId : request.getRemoveTriggerId()){
                        Trigger findTrigger= triggerRepository.findByTriggerGTMId(triggerId).orElseThrow(()->
                                new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Trigger " + triggerId + " does not exist")
                        );
                        Long deleteTagTrigger= tagTriggerRepository.deleteByTrigger(findTrigger);
                        if(deleteTagTrigger==0){
                            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Trigger " + triggerId + " does not exist");
                        }
                    }
                    removeTriggerFromGTM(tag, request.getRemoveTriggerId());
                }
                if(request.getTagGtmId()!=null){
                    String parent = String.format("accounts/%s/containers/%s/workspaces/%s/tags/%s", accountId, request.getContainerId(),request.getWorkspaceId(),request.getTagGtmId());
                    tagManager.accounts().containers().workspaces().tags().update(parent,tag).execute();
                }
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

    @Override
    public ResponseEntity<?> createTrigger(CreateTriggerRequest request) throws IOException {
        String parent = String.format("accounts/%s/containers/%s/workspaces/%s", accountId, 201397027, 2);
        com.google.api.services.tagmanager.model.Trigger newTrigger = new com.google.api.services.tagmanager.model.Trigger()
                .setName(request.getTriggerName())
                .setType(request.getTriggerType())  // Kiểu trigger, ví dụ: PAGEVIEW, CLICK, FORM_SUBMISSION
                .setParameter(null); // Thêm các tham số nếu cần

        com.google.api.services.tagmanager.model.Trigger createdTrigger = tagManager.accounts().containers()
                .workspaces().triggers()
                .create(parent, newTrigger)
                .execute();

        System.out.println("Trigger created with ID: " + createdTrigger.getTriggerId());
        return ResponseEntity.ok(createdTrigger.getTriggerId());
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
