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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tagId;

    @Column(name = "tag_gtm_id", length = 20,unique = true)
    String tagGtmId;

    @NotBlank(message = "tagName cannot be blank")
    @Column(name = "tag_name", nullable = false, unique = true)
    String tagName;

    @NotNull(message = "type cannot be null")
    @Column(name = "type", nullable = false)
    String type;

    @NotNull(message = "status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "varchar(20) default 'SAVE'")
    TagStatus status;

    @NotNull(message = "accountId cannot be null")
    @Column(name = "account_id", nullable = false)
    String accountId;

    @NotNull(message = "containerId cannot be null")
    @Column(name = "container_id", nullable = false)
    String containerId;

    @Column(name = "finger_print")
    String fingerPrint;

    @NotNull(message = "workspaceId cannot be null")
    @Column(name = "workspace_id")
    String workspaceId;

    @Column(name = "tag_firing_option")
    String tagFiringOption;

    @Column(name = "monitoring_metadata")
    String monitoringMetadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "db_tag_template",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "template_id")
    )
    Set<TemplateMaster> templateMasters;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "db_tag_parameter",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "parameter_id")
    )
    Set<ParameterMaster> parameterMasters;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(name = "db_tag_trigger", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "trigger_id"))
    Set<Trigger> triggers;
}