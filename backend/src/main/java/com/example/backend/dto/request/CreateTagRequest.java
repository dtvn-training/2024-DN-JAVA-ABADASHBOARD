package com.example.backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTagRequest {
    @NotNull(message = "tag name is required")
    private String tagName;
    @NotNull(message = "tag type is required")
    private String tagType;
}
