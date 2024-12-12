package com.example.backend.entity;

import com.example.backend.enums.MatchType;
import com.example.backend.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class Keyword extends AbstractDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    Long keywordId;

    @NotNull(message = "keywordText cannot be null")
    @Column(name = "keyword_text", nullable = false)
    String keywordText;

    @Enumerated(EnumType.STRING)
    @Column(name = "match_type", columnDefinition = "varchar(10) default 'Exact'")
    MatchType matchType;

    @Column(name = "cpc_bid")
    BigDecimal cpcBid;

    @Column(name = "search_volume")
    Integer searchVolume;

    @NotNull(message = "status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "keyword_status", nullable = false, columnDefinition = "varchar(10) default 'ACTIVE'")
    Status status;

    @NotNull(message = "adsGroup cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_group_id", nullable = false, referencedColumnName = "ad_group_id")
    AdsGroup adsGroup;
}