package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "db_event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event extends AbstractDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long eventId;

    @Column(name = "event_name", nullable = false, unique = true)
    String eventName;

    @NotNull(message = "eventLabel cannot be null")
    @Column(name = "event_label", nullable = false)
    String eventLabel;

    @NotBlank(message = "eventValue cannot be blank")
    @Column(name = "event_value", nullable = false)
    String eventValue;

    @Column(name = "timestamp")
    LocalDateTime timestamp;

    @Column(name = "update_at")
    LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id")
    Campaign campaign;

    @OneToMany(mappedBy = "event")
    private List<PurchaseRevenue> purchaseRevenues;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medium_id", referencedColumnName = "medium_id")
    Medium medium;
}