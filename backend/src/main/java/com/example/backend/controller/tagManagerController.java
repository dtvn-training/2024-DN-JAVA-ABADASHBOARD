package com.example.backend.controller;

import com.example.backend.dto.request.Tag.listTagRequestGTM;
import  com.example.backend.dto.request.Tag.listTagRequestGTM ;
import com.example.backend.service.GoogleTagManagerService.GoogleTagManagerService;
import com.google.api.services.tagmanager.model.Container;
import com.google.api.services.tagmanager.model.Tag;
import com.google.api.services.tagmanager.model.Workspace;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/google-tag-manager")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class tagManagerController {

    private final GoogleTagManagerService googleTagManagerService;


    @GetMapping ("/list-tag")
    public List<Tag> listTag(@RequestBody listTagRequestGTM request) throws Exception {
        Container container= googleTagManagerService.findContainerName("abalightacademy.com");

        return googleTagManagerService.listTags(request);
    }
}
