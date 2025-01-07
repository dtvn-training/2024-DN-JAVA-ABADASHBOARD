package com.example.backend.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TriggerTemplateResponse {
    private Long triggerTemplateId;
    private Integer key;
    private String displayName;
    private String vendorTemplatePublicId;
    private String groupDisplayName;
    private String groupDisplayNameAllEvents;
    private String groupDisplayNameSomeEvents;
}