package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "db_metric_master")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MetricMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    Long metricId;

    @NotBlank(message = "NOT_BLANK")
    @Column(name = "metric_key", nullable = false)
    private String metricKey;

    @NotNull(message = "NOT_NULL")
    @Column(name = "name", nullable = false)
    String name;

    @ManyToMany(mappedBy = "metricMasters")
    Set<DimensionMaster> dimensionMasters;
}
