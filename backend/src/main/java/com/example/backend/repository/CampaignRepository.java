package com.example.backend.repository;

import com.example.backend.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign,Long> {
    Optional<Campaign> findByCampaignId(Long campaignId);
    Optional<Campaign> findByCampaignName(String campaignName);
}
