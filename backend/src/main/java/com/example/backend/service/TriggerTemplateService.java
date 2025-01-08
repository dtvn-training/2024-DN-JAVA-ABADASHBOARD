package com.example.backend.service;

import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TriggerTemplateResponse;
import com.example.backend.entity.TriggerTemplate;
import java.util.List;

public interface TriggerTemplateService {
    PageResponse<TriggerTemplateResponse> listTriggerTemplates(int page, int size);
}