package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "db_medium")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Medium {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medium_id")
    Long mediumId;

    @Column(name = "medium_name", nullable = false, length = 50)
    private String mediumName;

    @OneToMany(mappedBy = "medium", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Event> events;
}
