package com.example.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.backend.enums.DeletedFlag;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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

//    @Size(max = 10, message = "This field must be 10 characters")
//    @Length(max = 10, message = "This field must be 10 characters")
    @NotNull(message = "NOT_NULL")
    @Enumerated(EnumType.STRING)
    @Column(name = "deleted_flag",nullable = false, columnDefinition = "varchar(50) default 'ACTIVE'")
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
