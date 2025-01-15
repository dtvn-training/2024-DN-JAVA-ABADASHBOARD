package com.example.backend.comon;

import com.example.backend.constant.AppConstant;
import com.example.backend.dto.response.ApiResponse;
import org.springframework.stereotype.Component;

@Component
public class CreateApiResponse {
    public static <T> ApiResponse<T> createResponse(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(AppConstant.CODE_SUCCESS);
        response.setMessage(AppConstant.MESSAGE_SUCCESS);
        response.setData(data);
        return response;
    }
}
