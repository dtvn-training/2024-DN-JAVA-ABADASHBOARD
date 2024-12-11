package com.example.backend.entity;

import com.example.backend.enums.TagStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Entity
@Table(name = "db_tag")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag extends AbstractDefault {
    @Id
    @Column(name = "tag_id")
    Long tagId;

    @NotBlank(message = "NOT_BLANK")
    @Column(name = "tag_name", nullable = false)
    String tagName;

    @NotNull(message = "NOT_NULL")
    @Column(name = "type", nullable = false)
    String type;

    @NotNull(message = "NOT_NULL")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) default 'SAVE'")
    TagStatus status;

    @NotNull(message = "NOT_NULL")
    @Column(name = "account_id")
    String accountId;

    @NotNull(message = "NOT_NULL")
    @Column(name = "container_id")
    String containerId;

    @Column(name = "finger_print")
    String fingerPrint;

    @NotNull(message = "NOT_NULL")
    @Column(name = "workspace_id")
    String workspaceId;

    @Column(name = "tag_firing_option")
    String tagFiringOption;

    @Column(name = "monitoring_metadata")
    String monitoringMetadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @ManyToMany(mappedBy = "tags")
    Set<TemplateMaster> templateMasters;

    @ManyToMany(mappedBy = "tags")
    Set<Trigger> triggers;
}
