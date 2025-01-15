package com.example.backend.repository;

import com.example.backend.entity.TagTrigger;
import com.example.backend.entity.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagTriggerRepository extends JpaRepository<TagTrigger, Long> {
    Long deleteByTrigger(Trigger trigger);
}
