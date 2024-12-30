package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;


@Entity
@Table(name = "db_dimension_master")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DimensionMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dimension_id")
    Long dimensionId;

    @NotBlank(message = "NOT_BLANK")
    @Column(name = "dimension_key", nullable = false)
    private String dimensionKey;

    @NotNull(message = "NOT_NULL")
    @Column(name = "name", nullable = false)
    String name;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "db_dimension_metric",
            joinColumns = @JoinColumn(name = "dimension_id"),
            inverseJoinColumns = @JoinColumn(name = "metric_id")
    )
    Set<MetricMaster> metricMasters;
}
