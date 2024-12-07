package com.example.backend.entity;

import com.example.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    Long campaignId;

    @Column(name = "campaign_name", nullable = false)
    String campaignName;

    @Column(name = "budget", nullable = false)
    BigDecimal budget;

//    @Column(name = "campaign_start_date")
//    Date campaignStartDate;
//
//    @Column(name = "campaign_end_date")
//    Date campaignEndDate;

    @Column(name = "target_audience")
    String targetAudience;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('active', 'inactive', 'pending') DEFAULT 'active'")
    Status status;

    @Column(name = "user_id")
    Long userId;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<AdsGroup> adsGroups;
}
