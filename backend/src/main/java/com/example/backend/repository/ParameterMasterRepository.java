package com.example.backend.repository;

import com.example.backend.entity.ParameterMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParameterMasterRepository extends JpaRepository<ParameterMaster, Long> {
    Optional<ParameterMaster> findByParameterKey(String parameterKey);
}
