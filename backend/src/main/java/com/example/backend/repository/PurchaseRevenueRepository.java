package com.example.backend.repository;

import com.example.backend.entity.PurchaseRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRevenueRepository extends JpaRepository<PurchaseRevenue,Long> {
    @Query(value = "SELECT source, COUNT(purchase_revenue_id) AS purchaseCount, SUM(amount) AS totalAmount " +
            "FROM db_purchase_revenue " +
            "GROUP BY source " +
            "ORDER BY totalAmount DESC", nativeQuery = true)
    List<Object[]> findPurchaseCountAndTotalAmountBySourceNative();
}