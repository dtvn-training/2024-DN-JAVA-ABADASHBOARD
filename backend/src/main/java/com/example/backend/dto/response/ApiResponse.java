package com.example.backend.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private int code = 200;
    private String message;
//    private List<T> data;
    private T data;
}

