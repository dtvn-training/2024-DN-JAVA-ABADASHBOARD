package com.example.backend.entity;

import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.*;
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
public class PageView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    Long pageId;

    @Column(name = "url", nullable = false)
    String url;

    @Column(name = "page_title", nullable = false)
    String pageTitle;

//    @Column(name = "update_at")
//    Timestamp updateAt;
//
//    @Column(name = "timestamp")
//    Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id")
    Campaign campaign;
}