package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "db_template_master")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TemplateMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    Long template_id;

    @Column(name = "key", columnDefinition = "TEXT")
    String key;

    @Column(name = "type", columnDefinition = "TEXT")
    String type;

    @Column(name = "value", columnDefinition = "TEXT")
    String value;

    @ManyToMany
    @JoinTable(
            name = "db_tag_template",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags ;

    @ManyToMany(mappedBy = "templateMasters")
    Set<Trigger> triggers;
}
