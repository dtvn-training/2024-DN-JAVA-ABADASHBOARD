package com.example.backend.entity;

import com.example.backend.enums.MatchType;
import com.example.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Table(name = "db_keyword")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    Long keywordId;

    @Column(name = "keyword_text", nullable = false)
    String keywordText;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", columnDefinition = "ENUM('broad', 'phrase', 'exact')")
    MatchType matchType;

    @Column(name = "cpc_bid")
    BigDecimal cpcBid;

    @Column(name = "search_volume")
    Integer searchVolume;

    @Enumerated(EnumType.STRING)
    @Column(name = "keyword_status", columnDefinition = "ENUM('active', 'inactive', 'paused')")
    Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_group_id", referencedColumnName = "ad_group_id")
    AdsGroup adsGroup;
}
