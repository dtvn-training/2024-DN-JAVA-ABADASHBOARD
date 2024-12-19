package com.example.backend.service.GoogleTagManagerService.Impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TagResponse;
import com.example.backend.entity.Tag;
import com.example.backend.enums.ErrorCode;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.TagMapper;
import com.example.backend.repository.TagRepository;
import com.example.backend.service.GoogleTagManagerService.TagService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.backend.dto.request.Tag.ListTagRequestGTM;
import com.google.api.services.tagmanager.TagManager;
import com.google.api.services.tagmanager.model.ListTagsResponse;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    @Value("${google-tag-manager.account_id}")
    private String accountId;

    private final TagManager tagManager;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final DataSource dataSource;

    @Override
    public List<com.google.api.services.tagmanager.model.Tag> listTagGTM(ListTagRequestGTM requestGTM) throws IOException {
        // Fetch tags from Google Tag Manager based on the provided container and workspace IDs.
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
        Sort sort = Sort.by(Sort.Direction.DESC,"createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Tag> tagPage = tagRepository.findAll(pageable);

        // Map the Tag entities to TagResponse DTOs
        List<TagResponse> tagResponses = tagPage.getContent().stream()
                .map(tagMapper::convertEntityToTagResponse)
                .collect(Collectors.toList());

        return createPageResponse(tagPage, tagResponses);
    }

    @Override
    public TagResponse getTagById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::convertEntityToTagResponse)
                .orElseThrow(() -> new ApiException(ErrorCode.BAD_REQUEST.getCode(), "Tag with id " + id + " not found"));
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

}
