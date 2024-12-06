package com.example.backend.entity;

import com.example.backend.enums.TagStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "db_tag")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag extends AbstractDefault{
    @Id
    @Column(name = "variable_id")
    Long tagId;

    @Column(name = "tag_name",columnDefinition = "TEXT")
    String tagName;

    @Column(name = "type")
    String type;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    TagStatus status;


    @Column(name = "account_id")
    String accountId;

    @Column(name = "container_id")
    String containerId;

    @Column(name = "finger_print")
    String fingerPrint;

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
