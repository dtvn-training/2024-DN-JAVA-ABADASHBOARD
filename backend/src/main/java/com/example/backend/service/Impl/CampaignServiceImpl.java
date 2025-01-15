package com.example.backend.service.Impl;

import com.example.backend.dto.CampaignDto;
import com.example.backend.dto.response.DropDownCampaignResponse;
import com.example.backend.dto.response.PageResponse;
import com.example.backend.entity.Campaign;
import com.example.backend.enums.ErrorCode;
import com.example.backend.enums.Status;
import com.example.backend.exception.ApiException;
import com.example.backend.mapper.CampaignMapper;
import com.example.backend.repository.CampaignRepository;
import com.example.backend.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private PageResponse<CampaignDto> createPageResponse(Page<Campaign> campaigns, List<CampaignDto> campaignDtos){
        PageResponse<CampaignDto> pageResponse= new PageResponse<>();
        pageResponse.setData(campaignDtos);
        pageResponse.setTotalPages(campaigns.getTotalPages());
        pageResponse.setTotalElements(campaigns.getTotalElements());
        pageResponse.setCurrentPage(campaigns.getNumber());
        pageResponse.setPageSize(campaigns.getSize());
        return pageResponse;
    }
    @Override
    public PageResponse<CampaignDto> getAllCampaigns(int pageNum, int pageSize) {
        try{
            Pageable pageable= PageRequest.of(pageNum, pageSize);
            Page<Campaign> campaigns= campaignRepository.findAll(pageable);
            List<CampaignDto> campaignDtos= campaigns.stream().map(this.campaignMapper::mapToDto).toList();
            return createPageResponse(campaigns, campaignDtos);
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(),e.getMessage());
        }
    }

    @Override
    public List<DropDownCampaignResponse> getAllDropDownCampaigns() {
        try{
            return campaignRepository.getCampaignsForDropDown();
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(),e.getMessage());
        }
    }

    @Override
    public CampaignDto getCampaignById(Long campaignId) {
        try{
            Campaign campaign = campaignRepository.findById(campaignId)
                    .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND.getStatusCode().value(),"Campaign not found"));
            return campaignMapper.mapToDto(campaign);
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(),e.getMessage());
        }
    }

    @Override
    public PageResponse<CampaignDto> getAllCampaignsByFilter(int pageNum, int pageSize,
                                                             String status,
                                                             double minBudget,
                                                             double maxBudget,
                                                             String startDate,
                                                             String endDate
    ) {
        try{
            Pageable pageable= PageRequest.of(pageNum, pageSize);
            Page<Campaign> campaigns= campaignRepository.findCampaignByFilters(Status.valueOf(status),
                                BigDecimal.valueOf(minBudget),
                                BigDecimal.valueOf(maxBudget),
                                startDate,endDate,pageable);
            List<CampaignDto> campaignDtos= campaigns.stream().map(this.campaignMapper::mapToDto).toList();
            return createPageResponse(campaigns, campaignDtos);
        }catch (Exception e){
            throw new ApiException(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode().value(),e.getMessage());
        }
    }
}
