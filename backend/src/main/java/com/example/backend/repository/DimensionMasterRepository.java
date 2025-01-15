package com.example.backend.repository;

import com.example.backend.entity.DimensionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DimensionMasterRepository extends JpaRepository<DimensionMaster,Long> {
    Optional<DimensionMaster> findByDimensionKey(String dimensionKey);
}
