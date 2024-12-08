package com.example.backend.dto.request.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListTagRequestGTM {
    @NotBlank(message = "NOT_BLANK")
    String containerId;

    @NotBlank(message = "NOT_BLANK")
    String workspaceId;
}
