package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "db_event")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    Long eventId;

    @Column(name = "event_name", nullable = false, unique = true)
    String eventName;

    @Column(name = "event_label", nullable = false)
    String eventLabel;

    @Column(name = "event_value", nullable = false)
    String eventValue;

    @Column(name = "timestamp")
    LocalDateTime timestamp;

    @Column(name = "update_at")
    LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id")
    Campaign campaign;
}