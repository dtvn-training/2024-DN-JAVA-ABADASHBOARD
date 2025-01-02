package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "key cannot be blank")
    @Column(nullable = false)
    private Integer key;

    @NotBlank(message = "displayName cannot be blank")
    @Column(nullable = false)
    private String displayName;

    private String vendorTemplatePublicId;

    @NotBlank(message = "groupDisplayName cannot be blank")
    @Column(nullable = false)
    private String groupDisplayName;

    @NotBlank(message = "groupDisplayNameAllEvents cannot be blank")
    @Column(nullable = false)
    private String groupDisplayNameAllEvents;

    @NotBlank(message = "groupDisplayNameSomeEvents cannot be blank")
    @Column(nullable = false)
    private String groupDisplayNameSomeEvents;

    @ManyToMany(mappedBy = "triggerTemplates")
    Set<Trigger> triggers;
}