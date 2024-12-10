package com.example.backend.entity;

import com.example.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "db_campaign")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Campaign extends AbstractDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    Long campaignId;

    @Column(name = "campaign_name", nullable = false)
    String campaignName;

    @Column(name = "budget", nullable = false)
    BigDecimal budget;

    @Column(name = "campaign_start_date")
    LocalDateTime campaignStartDate;

    @Column(name = "campaign_end_date")
    LocalDateTime campaignEndDate;

    @Column(name = "target_audience")
    String targetAudience;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(255) default 'ACTIVE'")
    Status status;

    @Column(name = "user_id")
    Long userId;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<AdsGroup> adsGroups;
}
