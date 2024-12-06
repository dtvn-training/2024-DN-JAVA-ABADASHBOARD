package com.example.backend.entity;

import jakarta.persistence.*;
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
public class Varriable{
    @Id
    @Column(name = "variable_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long variableId;

    @Column(name = "variable_name",columnDefinition = "TEXT")
    String variableName;

    @Column(name = "value")
    String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

}
