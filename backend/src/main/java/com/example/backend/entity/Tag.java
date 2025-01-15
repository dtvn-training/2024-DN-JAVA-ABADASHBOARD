package com.example.backend.entity;

import com.example.backend.enums.TagStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
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

    @NotBlank(message = "NOT_BLANK")
    @Column(name = "tag_name", nullable = false, unique = true)
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

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private Set<TagTrigger> tagTriggers = new HashSet<>();
}
