package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "db_variable")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Variable extends AbstractDefault {
    @Id
    @Column(name = "variable_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long variableId;

    @NotBlank(message = "NOT_BLANK")
    @Column(name = "variable_name", nullable = false)
    String variableName;

    @NotNull(message = "NOT_NULL")
    @Column(name = "value", nullable = false)
    String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

}
