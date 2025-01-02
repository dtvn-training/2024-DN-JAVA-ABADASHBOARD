package com.example.backend.repository;

import com.example.backend.entity.PurchaseRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRevenueRepository extends JpaRepository<PurchaseRevenue,Long> {
}
