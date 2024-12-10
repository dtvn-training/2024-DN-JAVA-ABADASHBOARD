package com.example.backend.repository;

import com.example.backend.entity.TemplateMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateMasterRepository extends JpaRepository<TemplateMaster, Long> {
    Optional<TemplateMaster> findByType(String type);
}
