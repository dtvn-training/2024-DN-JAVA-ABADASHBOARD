package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "db_trigger")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trigger extends AbstractDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "path", nullable = false, length = 255)
    private String path;

    @Column(name = "account_id", nullable = false, length = 255)
    private String accountId;

    @Column(name = "container_id", nullable = false, length = 255)
    private String containerId;

    @Column(name = "workspace_id", nullable = false, length = 255)
    private String workspaceId;

    @Column(name = "trigger_id", nullable = false, length = 255)
    private String triggerId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "type", nullable = false, length = 255)
    private String type;

    @Column(name = "fingerprint", length = 255)
    private String fingerprint;

    @Column(name = "parent_folder_id", length = 255)
    private String parentFolderId;

    @Column(name = "tag_manager_url", length = 255)
    private String tagManagerUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToMany
    @JoinTable(
            name = "db_tag_trigger",
            joinColumns = @JoinColumn(name = "trigger_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags;

    @ManyToMany
    @JoinTable(
            name = "db_trigger_template",
            joinColumns = @JoinColumn(name = "trigger_id"),
            inverseJoinColumns = @JoinColumn(name = "template_id"))
    Set<TemplateMaster> templateMasters;

}
