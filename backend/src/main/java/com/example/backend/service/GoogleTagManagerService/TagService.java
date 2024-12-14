package com.example.backend.service.GoogleTagManagerService;

import com.example.backend.dto.request.Tag.ListTagRequestGTM;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TagResponse;
import com.google.api.services.tagmanager.model.Tag;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface TagService {
    List<Tag> listTagGTM(ListTagRequestGTM requestGTM) throws IOException;
    PageResponse<TagResponse> listTags(int page, int size);
    TagResponse getTagById(Long id);
}