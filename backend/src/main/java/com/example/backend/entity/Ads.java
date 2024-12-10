package com.example.backend.entity;

import jakarta.persistence.*;
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

    @Column(name = "ads_name", nullable = false)
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_group_id", referencedColumnName = "ad_group_id", nullable = false)
    AdsGroup adsGroup;
}
