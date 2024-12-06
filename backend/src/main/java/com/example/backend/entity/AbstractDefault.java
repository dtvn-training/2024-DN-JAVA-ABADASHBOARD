package com.example.backend.entity;

import com.example.backend.enums.DeletedFlag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class AbstractDefault {
    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    LocalDateTime updatedAt;

    @Column(name = "updated_by", columnDefinition = "TEXT DEFAULT ''")
    String updatedBy;

    @Column(name = "created_by", columnDefinition = "TEXT DEFAULT ''")
    String createdBy;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "deleted_flag")
    DeletedFlag deletedFlag;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
