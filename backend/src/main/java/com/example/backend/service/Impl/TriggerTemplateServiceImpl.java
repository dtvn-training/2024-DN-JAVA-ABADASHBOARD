package com.example.backend.service.Impl;

import com.example.backend.dto.response.PageResponse;
import com.example.backend.dto.response.TriggerTemplateResponse;
import com.example.backend.entity.Tag;
import com.example.backend.entity.TriggerTemplate;
import com.example.backend.mapper.TriggerTemplateMapper;
import com.example.backend.repository.TriggerTemplateRepository;
import com.example.backend.service.TriggerTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TriggerTemplateServiceImpl implements TriggerTemplateService {

    private final TriggerTemplateRepository triggerTemplateRepository;
    private  final TriggerTemplateMapper triggerTemplateMapper;

    @Override
    public  PageResponse<TriggerTemplateResponse> listTriggerTemplates (int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<TriggerTemplate>  triggerTemplatesPage = triggerTemplateRepository.findAll(pageable);

        List<TriggerTemplateResponse> triggerTemplateResponses = triggerTemplatesPage.getContent().stream()
                .map(triggerTemplateMapper::convertEntityToResponse)
                .collect(Collectors.toList());

        return createPageResponse(triggerTemplatesPage, triggerTemplateResponses);
    }

    private PageResponse<TriggerTemplateResponse> createPageResponse(Page<TriggerTemplate> pageData, List<TriggerTemplateResponse> triggerTemplateResponse) {
        return PageResponse.<TriggerTemplateResponse>builder()
                .currentPage(pageData.getNumber() + 1)
                .totalPages(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .data(triggerTemplateResponse)
                .build();
    }
}