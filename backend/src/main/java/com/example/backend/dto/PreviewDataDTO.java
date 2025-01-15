package com.example.backend.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreviewDataDTO<T> {
    private T data;
    private String header;
    private List<String> categories;

}

