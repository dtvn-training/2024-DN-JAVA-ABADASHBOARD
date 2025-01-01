package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@ToString
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
    Long parameter_id;

    @NotBlank(message = "key cannot be blank")
    @Column(name = "parameter_key", nullable = false)
    String key;

    @NotNull(message = "type cannot be null")
    @Column(name = "type", nullable = false)
    String type;

    @ManyToMany(mappedBy = "parameterMasters")
    Set<Tag> tags;
}