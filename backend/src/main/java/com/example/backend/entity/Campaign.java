package com.example.backend.entity;

import com.example.backend.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.aspectj.bridge.IMessage;

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

    @NotBlank(message = "campaignName cannot be blank")
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

    @NotNull(message = "status cannot be null")
    @Size(max = 10, message = "status cannot exceed 10 characters.")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'ACTIVE'")
    Status status;

    @NotNull(message = "userId cannot be null")
    @Column(name = "user_id")
    Long userId;

    @OneToMany(mappedBy = "campaign", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<AdsGroup> adsGroups;
}