package com.example.backend.repository;

import com.example.backend.dto.response.DropDownCampaignResponse;
import com.example.backend.entity.Campaign;
import com.example.backend.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,Long> {
    Optional<Campaign> findByCampaignId(Long campaignId);

    @Query("select new com.example.backend.dto.response.DropDownCampaignResponse(c.campaignId,c.campaignName) from Campaign c")
    List<DropDownCampaignResponse> getCampaignsForDropDown();


    @Query("""
         select c from Campaign c
         where c.status = :status
         and c.budget between :minBudget and :maxBudget
         and date_trunc('day',c.campaignStartDate)=date_trunc('day',cast(:startDate as timestamp))
         and date_trunc('day',c.campaignEndDate)=date_trunc('day',cast(:endDate as timestamp))
""")
    Page<Campaign> findCampaignByFilters(
            @Param("status") Status status,
            @Param("minBudget") BigDecimal minBudget,
            @Param("maxBudget") BigDecimal maxBudget,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            Pageable pageable
    );

}
