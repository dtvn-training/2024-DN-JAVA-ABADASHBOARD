package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.request.Tag.ListTagRequestGTM;
import com.example.backend.dto.response.ApiResponse;
import com.example.backend.service.GoogleTagManagerServiceImpl;
import com.google.api.services.tagmanager.model.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/google-tag-manager")
@RequiredArgsConstructor
public class TagManagerController {

    private final GoogleTagManagerServiceImpl googleTagManagerServiceImpl;

    @GetMapping("/list-tag")
    public ResponseEntity<ApiResponse<Tag>> listTag(@RequestBody ListTagRequestGTM request) {
        try {
            List<Tag> tags = googleTagManagerServiceImpl.listTags(request);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Success", tags));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
        }
    }
}
