package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "db_parameter_master")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParameterMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id")
    Long template_id;

    @Column(name = "parameter_key", nullable = false)
    private String parameterKey;

    @Column(name = "type", nullable = false)
    String type;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "db_tag_template",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags;
}
