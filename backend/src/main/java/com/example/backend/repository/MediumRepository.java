package com.example.backend.repository;

import com.example.backend.entity.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediumRepository extends JpaRepository<Medium,Long> {
    Optional<Medium> findByMediumName(String mediumName);
}
