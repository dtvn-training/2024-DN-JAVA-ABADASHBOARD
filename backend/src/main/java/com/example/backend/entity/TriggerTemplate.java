package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "db_trigger_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TriggerTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trigger_template_id")
    private Long triggerTemplateId;
    @Column( nullable = false)
    private Integer key;
    @Column(nullable = false)
    private String displayName;
    private String vendorTemplatePublicId;
    @Column(nullable = false)
    private String groupDisplayName;
    @Column(nullable = false)
    private String groupDisplayNameAllEvents;
    @Column(nullable = false)
    private String groupDisplayNameSomeEvents;
    @ManyToMany(mappedBy = "triggerTemplates")
    Set<Trigger> triggers;
}
