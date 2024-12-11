package com.example.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
        @Column(name = "trigger_id")
        private Long triggerId;

        @NotNull(message = "NOT_NULL")
        @Size(max = 255, message = "path cannot exceed 255 characters.")
        @Column(name = "path", nullable = false)
        private String path;

        @NotNull(message = "NOT_NULL")
        @Size(max = 20, message = "accountId cannot exceed 20 characters.")
        @Column(name = "account_id", nullable = false)
        private String accountId;

        @NotNull(message = "NOT_NULL")
        @Size(max = 20, message = "containerId cannot exceed 20 characters.")
        @Column(name = "container_id", nullable = false)
        private String containerId;

        @NotNull(message = "NOT_NULL")
        @Size(max = 20, message = "workspaceId cannot exceed 20 characters.")
        @Column(name = "workspace_id", nullable = false)
        private String workspaceId;

        @NotBlank(message = "NOT_BLANK")
        @Size(max = 20, message = "name cannot exceed 20 characters.")
        @Column(name = "name", nullable = false)
        private String name;

        @NotNull(message = "NOT_NULL")
        @Size(max = 50, message = "type cannot exceed 50 characters.")
        @Column(name = "type", nullable = false)
        private String type;

        @Size(max = 255, message = "fingerprint cannot exceed 255 characters.")
        @Column(name = "fingerprint")
        private String fingerprint;

        @Size(max = 20, message = "type cannot exceed 20 characters.")
        @Column(name = "parent_folder_id")
        private String parentFolderId;

        @Size(min = 10, max = 500, message = "tagManagerUrl must be between 10 and 500 characters")
        @Column(name = "tag_manager_url")
        private String tagManagerUrl;

        @Size(min = 10, max = 500, message = "notes must be between 10 and 500 characters")
        @Column(name = "notes")
        private String notes;

        @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
        @JoinTable(name = "db_tag_trigger", joinColumns = @JoinColumn(name = "trigger_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
        Set<Tag> tags;

        @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
        @JoinTable(name = "db_trigger_template", joinColumns = @JoinColumn(name = "trigger_id"), inverseJoinColumns = @JoinColumn(name = "template_id"))
        Set<TemplateMaster> templateMasters;

}
