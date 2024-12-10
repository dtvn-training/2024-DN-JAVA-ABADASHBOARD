package com.example.backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "db_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    Long role_id;

    @NotNull(message = "NOT_NULL")
    @Column(name = "role_name")
    String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    List<User> users;
}
