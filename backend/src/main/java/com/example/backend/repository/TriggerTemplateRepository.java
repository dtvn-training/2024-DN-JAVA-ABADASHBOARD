package com.example.backend.repository;

import com.example.backend.entity.TriggerTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TriggerTemplateRepository extends JpaRepository<TriggerTemplate, Long> {
    Optional<TriggerTemplate> findByKey(int key);
}
