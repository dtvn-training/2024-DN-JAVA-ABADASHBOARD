package com.example.backend.repository;

import com.example.backend.entity.MetricMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricMasterRepository extends JpaRepository<MetricMaster,Long> {
    Optional<MetricMaster> findByMetricKey(String metricKey);
}
