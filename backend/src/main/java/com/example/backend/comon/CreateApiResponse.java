package com.example.backend.comon;

import com.example.backend.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class CreateApiResponse {
    public static <T> ApiResponse<T> createResponse(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
}
