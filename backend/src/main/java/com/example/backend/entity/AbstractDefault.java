package com.example.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.backend.enums.DeletedFlag;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractDefault {

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_flag",nullable = false, columnDefinition = "varchar(255) default 'ACTIVE'")
    private DeletedFlag deletedFlag;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
