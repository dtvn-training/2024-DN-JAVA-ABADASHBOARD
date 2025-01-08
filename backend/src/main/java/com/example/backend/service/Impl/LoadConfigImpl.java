package com.example.backend.service.Impl;

import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.service.LoadConfig;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.Container;
import com.google.api.services.tagmanager.model.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadConfigImpl implements LoadConfig {
    @Value("${google-tag-manager.account_id}")
    private String accountId;
    private final TagManager tagManager;

    @Override
    public List<Container> getAllContainer() {
        try{
            String parent = String.format("accounts/%s", accountId);
            return tagManager.accounts().containers().list(parent).execute().getContainer();
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMessage());
        }
    }

    @Override
    public List<Workspace> getAllWorkspace(String containerId) {
        try{
            String parent = String.format("accounts/%s/containers/%s", accountId, containerId);
            return tagManager.accounts().containers().workspaces().list(parent).execute().getWorkspace();
        }catch (Exception e){
            throw new ApiException(ErrorCode.BAD_REQUEST.getCode(), ErrorCode.BAD_REQUEST.getMessage());
        }
    }
}
