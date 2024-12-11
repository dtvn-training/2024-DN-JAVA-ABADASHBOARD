package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "db_ads")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Ads extends AbstractDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ads_id")
    Long adsId;

    @NotBlank(message = "NOT_BLANK")
    @Column(name = "ads_name")
    String name;

    @NotNull(message = "NOT_NULL")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_group_id", referencedColumnName = "ad_group_id", nullable = false)
    AdsGroup adsGroup;
}
