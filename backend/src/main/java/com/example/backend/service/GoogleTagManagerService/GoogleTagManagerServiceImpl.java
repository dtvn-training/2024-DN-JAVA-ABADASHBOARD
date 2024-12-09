package com.example.backend.service.GoogleTagManagerService;

import com.example.backend.dto.response.ApiResponse;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.Container;
import com.google.api.services.tagmanager.model.Parameter;
import com.google.api.services.tagmanager.model.Tag;
import com.google.api.services.tagmanager.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleTagManagerServiceImpl implements GoogleTagManagerService {
    @Value("${google-tag-manager.account_id}")
    private String accountId;
    @Value("${google-analytic.tracking-id}")
    private String trackingId;


    private final TagManager tagManager;

    private  Container findContainerName(TagManager service, String containerName)
            throws Exception {
        String parent = String.format("accounts/%s", accountId);
        System.out.println(accountId);
        for (Container container :
                service.accounts().containers().list(parent).execute().getContainer()) {
            if (container.getName().equals(containerName)) {
                return container;
            }
        }
        throw new IllegalArgumentException("No container named Greetings in given account");
    }

    private List<Workspace> findWorkspace(TagManager service, String containerId) throws IOException {
        String parent = String.format("accounts/%s/containers/%s", accountId, containerId);
        return service.accounts().containers().workspaces().list(parent).execute().getWorkspace();
    }

//    public Tag createTag(TagManager service, String containerId, String workspaceId, String tagName, String type) throws IOException {
//        String parent = String.format("accounts/%s/containers/%s/workspaces/%s", accountId, containerId,workspaceId);
//
//        Tag tag = new Tag();
//        tag.setName(tagName);
//        tag.setType(type);
//
//        List<Parameter> parameters= new ArrayList<>();
//        Parameter eventName = new Parameter();
//        eventName.setKey("eventName");
//        eventName.setType("template");
//        eventName.setValue("page_view");
//
//        Parameter measurementId = new Parameter();
//        measurementId.setKey("measurementIdOverride");
//        measurementId.setType("template");
//        measurementId.setValue(trackingId);
//
//        parameters.add(eventName);
//        parameters.add(measurementId);
//        tag.setParameter(parameters);
//        tag= service.accounts().containers().workspaces().tags().create(parent,tag).execute();
//        return tag;
//
//    }

    @Override
    public ApiResponse<?> CreateTag(String tagName, String tagType) {
        if (tagName == null || tagName.isEmpty() || tagType == null || tagType.isEmpty()) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Tag name or tag type cannot be null or empty");
        }

        try {
            return ApiResponse.builder()
                    .code(200)
                    .message("Create tag successfully")
                    .data(List.of("string"))
                    .build();
        } catch (Exception e) {
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMessage());
        }
    }
}
