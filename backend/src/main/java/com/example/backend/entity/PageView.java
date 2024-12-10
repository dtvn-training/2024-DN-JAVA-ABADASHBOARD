package com.example.backend.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "db_pageview")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageView extends AbstractDefault{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    Long pageId;

    @NotNull(message = "NOT_NULL")
    @Column(name = "url")
    String url;

    @NotBlank(message = "NOT_BLANK")
    @Column(name = "page_title")
    String pageTitle;

    @Column(name = "update_at")
    LocalDateTime updateAt;

    @Column(name = "timestamp")
    LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id")
    Campaign campaign;
}