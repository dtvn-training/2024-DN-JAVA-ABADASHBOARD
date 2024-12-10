package com.example.backend.entity;

import com.example.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "db_ads_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdsGroup extends AbstractDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_group_id")
    Long adGroupId;

    @Column(name = "ad_group_name", nullable = false)
    String adGroupName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ad_group_status",nullable = false, columnDefinition = "varchar(50) default 'ACTIVE'")
    Status status;

    @Column(name = "targeting_criteria")
    String targetingCriteria;

    @Column(name = "bid_amount")
    BigDecimal bidAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    Campaign campaign;

    @OneToMany(mappedBy = "adsGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Keyword> keywords;

    @OneToMany(mappedBy = "adsGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Ads> ads;
}
