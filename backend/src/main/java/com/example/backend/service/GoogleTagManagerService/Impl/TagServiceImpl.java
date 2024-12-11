package com.example.backend.service.GoogleTagManagerService.Impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend.dto.response.TagResponse;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.TagRepository;
import com.example.backend.service.GoogleTagManagerService.TagService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.backend.dto.request.Tag.ListTagRequestGTM;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.ListTagsResponse;
import com.google.api.services.tagmanager.model.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public  class TagServiceImpl implements TagService {

    @Value("${google-tag-manager.account_id}")
    private String accountId;

    private final TagManager tagManager;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<Tag> listTagGTM(ListTagRequestGTM requestGTM) throws IOException {
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
    public List<TagResponse> listTags() {
            return tagRepository.findAll().stream()
                    .map(tagMapper::toTagResponse)
                    .collect(Collectors.toList());
    }

}
