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
    @Column(name = "tag_id")
    Long tagId;

    @Column(name = "tag_name",nullable = false)
    String tagName;

    @Column(name = "type",nullable = false)
    String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false, columnDefinition = "varchar(20) default 'SAVE'")
    TagStatus status;


    @Column(name = "account_id",nullable = false)
    String accountId;

    @Column(name = "container_id",nullable = false)
    String containerId;

    @Column(name = "finger_print")
    String fingerPrint;

    @Column(name = "workspace_id",nullable = false)
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
