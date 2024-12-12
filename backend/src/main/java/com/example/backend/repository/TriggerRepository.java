package com.example.backend.repository;

import com.example.backend.entity.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long> {
}
