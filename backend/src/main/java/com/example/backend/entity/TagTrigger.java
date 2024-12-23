package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "db_tag_trigger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagTrigger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_trigger_id")
    private Long tagTriggerId;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "trigger_id")
    private Trigger trigger;

    private boolean isPositive;
}
