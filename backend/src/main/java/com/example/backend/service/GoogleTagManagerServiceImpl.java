package com.example.backend.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.backend.dto.request.Tag.ListTagRequestGTM;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.ListTagsResponse;
import com.google.api.services.tagmanager.model.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleTagManagerServiceImpl implements GoogleTagManagerService {

    @Value("${google-tag-manager.account_id}")
    private String accountId;

    private final TagManager tagManager;

    @Override
    public List<Tag> listTags(ListTagRequestGTM requestGTM) throws IOException {
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
}
